package com.insurance.insurance.service;

import com.insurance.insurance.dto.HealthJoinDTO;
import com.insurance.insurance.dto.HealthRequestDTO;
import com.insurance.insurance.entity.*;
import com.insurance.insurance.exception.DataNotFoundException;
import com.insurance.insurance.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InsuranceService {
    private final InsuranceRepository insuranceRepository;
    private final FireInsuranceRepository fireInsuranceRepository;
    private final AutoInsuranceRepository autoInsuranceRepository;
    private final HealthInsuranceRepository healthInsuranceRepository;

    private final UserInfoRepository userInfoRepository;
    private final ProductRepository productRepository;
    private final RiskRankRepository riskRankRepository;
    private final ProductPriceRepository productPriceRepository;


    public List<Insurance> getBySiteUser(SiteUser siteUser) {
        return insuranceRepository.findAllBySiteUser(siteUser);
    }

    public FireInsurance getFireBySiteUser(SiteUser siteUser) {
        Product product = productRepository.findByName("화재보험");
        List<Insurance> insuranceList = insuranceRepository.findBySiteUserAndProduct(siteUser,product);
        Insurance insurance = new Insurance();

        FireInsurance fireInsurance = fireInsuranceRepository.findByInsuranceAndPropertyAddressAndBuildingType(insurance,"임시","임시")
                .orElseThrow(()->new DataNotFoundException("해당 가입내용이 존재하지 않습니다."));
        return fireInsurance;
    }

    public AutoInsurance getAutoBySiteUser(SiteUser siteUser) {
        Product product = productRepository.findByName("자동차보험");
        List<Insurance> insuranceList = insuranceRepository.findBySiteUserAndProduct(siteUser,product);
        Insurance insurance = new Insurance();
        AutoInsurance autoInsurance = autoInsuranceRepository.findByInsuranceAndVehicleNumber(insurance,"임시")
                .orElseThrow(()->new DataNotFoundException("해당 가입내용이 존재하지 않습니다."));
        return autoInsurance;
    }

    public HealthInsurance getHealthBySiteUser(SiteUser siteUser) {
        Product product = productRepository.findByName("건강보험");
        List<Insurance> insuranceList = insuranceRepository.findBySiteUserAndProduct(siteUser,product);
        Insurance insurance = new Insurance();
        HealthInsurance healthInsurance = healthInsuranceRepository.findByInsurance(insurance)
                .orElseThrow(()->new DataNotFoundException("해당 가입내용이 존재하지 않습니다."));
        return healthInsurance;
    }


    private Insurance create(SiteUser siteUser, HealthJoinDTO healthJoinDTO,
                             int riskScore, String rank, String type) {

        Product product = productRepository.findByName(type);
        ProductPrice productPrice = productPriceRepository.findByProduct(product);
        RiskRank riskRank = riskRankRepository.findByName(rank);
        int price = (int) (riskRank.getPrice_rate() * productPrice.getPrice());
        String bank = healthJoinDTO.getBank();
        String bankAccount = healthJoinDTO.getAccount();
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusYears(healthJoinDTO.getDuration());

        Insurance insurance = new Insurance(siteUser, product, riskScore, riskRank, price, bank, bankAccount, startDate, endDate);

        return insuranceRepository.save(insurance);
    }



    @Async
    public void createHealth(SiteUser siteUser, HealthJoinDTO healthJoinDTO) {

        //리스크점수 확인
        UserInfo userInfo = userInfoRepository.findBySiteUser(siteUser).orElseThrow(
                ()->new DataNotFoundException("해당 유저가 없습니다."));
        int age = Period.between(userInfo.getBirthDay(), LocalDate.now()).getYears();
        int riskScore = calculateHealthRiskScore(healthJoinDTO,age);
        String rank = getCoveragePlan(riskScore);
        Insurance insurance = create(siteUser,healthJoinDTO,riskScore,rank,"건강보험");

        HealthInsurance healthInsurance = new HealthInsurance();
        healthInsurance.setInsurance(insurance);

        healthInsurance.setFamily(String.join(", ", healthJoinDTO.getFamily()));
        healthInsurance.setPreExistingConditions(String.join(", ", healthJoinDTO.getPre_existing_conditions()));
        healthInsuranceRepository.save(healthInsurance);
    }






    private int calculateHealthRiskScore(HealthJoinDTO healthJoinDTO, int age) {
        int riskScore = 0;

        if (age>50){
            riskScore += 10;
        }


        // 흡연 여부
        if (Boolean.TRUE.equals(healthJoinDTO.getSmoke())) {
            riskScore += 20;
        }

        // 음주 횟수
        riskScore += healthJoinDTO.getDrink() * 5;


        // 운동 시간
        if (healthJoinDTO.getExercise() != null) {
            riskScore -= healthJoinDTO.getExercise() * 3;
        }

        // 가족력
        if (healthJoinDTO.getFamily() != null) {
            riskScore += healthJoinDTO.getFamily().size() * 15;
        }

        // 기존 병력
        if (healthJoinDTO.getPre_existing_conditions() != null) {
            riskScore += healthJoinDTO.getPre_existing_conditions().size() * 20;
        }

        // 수술 여부
        if (Boolean.TRUE.equals(healthJoinDTO.getSurgery())) {
            riskScore += 20;
        }

        return Math.min(Math.max(riskScore, 0), 100);
        // 점수가 음수가 되지 않도록 보정
    }
    private String getCoveragePlan(int riskScore) {
        if (riskScore >= 0 && riskScore <= 30) {
            return "Full Coverage";
        } else if (riskScore >= 31 && riskScore <= 60) {
            return "Major Coverage";
        } else if (riskScore >= 61 && riskScore <= 80) {
            return "Essential Coverage";
        } else {
            return "Emergency Only";
        }
    }

}






