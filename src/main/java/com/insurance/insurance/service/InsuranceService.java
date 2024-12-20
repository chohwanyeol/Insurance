package com.insurance.insurance.service;

import com.insurance.insurance.dto.*;
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
    private final RenewableInsuranceRepository renewableInsuranceRepository;
    private final ProductService productService;
    private final UserInfoService userInfoService;
    private final RiskRankService riskRankService;



    public List<Insurance> getBySiteUser(SiteUser siteUser) {
        return insuranceRepository.findAllBySiteUser(siteUser);
    }

    public Insurance getBySiteUserAndId(SiteUser siteUser, Integer id) {
        return insuranceRepository.findBySiteUserAndId(siteUser, id)
                .orElseThrow(() -> new DataNotFoundException("해당 데이터가 존재하지 않습니다."));
    }

    public List<FireInsurance> getFireBySiteUser(SiteUser siteUser) {
        List<FireInsurance> fireInsuranceList = fireInsuranceRepository.findAllBySiteUser(siteUser)
                .orElseThrow(() -> new DataNotFoundException("해당 가입내용이 존재하지 않습니다."));
        return fireInsuranceList;
    }

    public FireInsurance getFireBySiteUserAndId(SiteUser siteUser, int id) {
        return fireInsuranceRepository.findBySiteUserAndId(siteUser,id)
                .orElseThrow(() -> new DataNotFoundException("해당 데이터가 존재하지 않습니다."));
    }

    public List<AutoInsurance> getAutoBySiteUser(SiteUser siteUser) {
        Product product = productService.getByName("자동차보험");
        List<AutoInsurance> autoInsuranceList = autoInsuranceRepository.findAllBySiteUser(siteUser)
                .orElseThrow(() -> new DataNotFoundException("해당 가입내용이 존재하지 않습니다."));

        return autoInsuranceList;
    }

    public AutoInsurance getAutoBySiteUserAndId(SiteUser siteUser, Integer id) {
        return autoInsuranceRepository.findBySiteUserAndId(siteUser,id)
                .orElseThrow(() -> new DataNotFoundException("해당 데이터가 존재하지 않습니다."));
    }

    public HealthInsurance getHealthBySiteUser(SiteUser siteUser) {
        HealthInsurance healthInsurance = healthInsuranceRepository.findBySiteUser(siteUser)
                .orElseThrow(() -> new DataNotFoundException("해당 가입내용이 존재하지 않습니다."));
        return healthInsurance;
    }

    public List<RenewableInsurance> getRenewableBySiteUser(SiteUser siteUser) {
        return renewableInsuranceRepository.findBySiteUser(siteUser);
    }

    public RenewableInsurance getRenewableBySiteUserAndId(SiteUser siteUser, Integer id) {
        return renewableInsuranceRepository.findBySiteUserAndId(siteUser, id);
    }

    private <T extends Insurance> T create(SiteUser siteUser, InsuranceJoinDTO insuranceJoinDTO, String type, T insurance) {
        UserInfo userInfo = siteUser.getUserInfo();
        int age = Period.between(userInfo.getBirthDay(), LocalDate.now()).getYears();

        int riskScore = 0;
        String rank = "";
        switch (type) {
            case "건강보험":
                riskScore = calculateHealthRiskScore((HealthJoinDTO) insuranceJoinDTO, age);
                rank = getCoveragePlan(riskScore);
                break;
            case "자동차보험":
                riskScore = calculateAutoRiskScore((AutoJoinDTO) insuranceJoinDTO, age);
                rank = getCoveragePlan(riskScore);
                break;
            case "화재보험":
                riskScore = calculateFireRiskScore((FireJoinDTO) insuranceJoinDTO);
                rank = getCoveragePlan(riskScore);
                break;
        }

        Product product = productService.getByName(type);
        RiskRank riskRank = riskRankService.getByNameAndProduct(rank, product);

        int price = (int) (riskRank.getPrice_rate() * product.getPrice());
        String bank = insuranceJoinDTO.getBank();
        String bankAccount = insuranceJoinDTO.getAccount();
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusYears(insuranceJoinDTO.getDuration());

        // 부모 클래스 필드 설정
        insurance.setSiteUser(siteUser);
        insurance.setProduct(product);
        insurance.setRiskRank(riskRank);
        insurance.setPrice(price);
        insurance.setStatus("pending");
        insurance.setStartDate(startDate);
        insurance.setEndDate(endDate);
        insurance.setBank(bank);
        insurance.setBankAccount(bankAccount);

        return insuranceRepository.save(insurance);
    }

    public Insurance cancel(Insurance insurance) {
        insurance.setStatus("cancelled");
        return insuranceRepository.save(insurance);
    }

    @Async
    public HealthInsurance createHealth(SiteUser siteUser, HealthJoinDTO healthJoinDTO) {
        HealthInsurance healthInsurance = new HealthInsurance();
        healthInsurance = create(siteUser, healthJoinDTO, "건강보험",healthInsurance);
        healthInsurance.setFamily(String.join(", ", healthJoinDTO.getFamily()));
        healthInsurance.setPreExistingConditions(String.join(", ", healthJoinDTO.getPre_existing_conditions()));
        return healthInsuranceRepository.save(healthInsurance);
    }

    @Async
    public AutoInsurance createAuto(SiteUser siteUser, AutoJoinDTO autoJoinDTO) {

        AutoInsurance autoInsurance = new AutoInsurance();
        autoInsurance = create(siteUser, autoJoinDTO, "자동차보험", autoInsurance);
        autoInsurance.setVehicleNumber(autoJoinDTO.getVehicleNumber());
        autoInsurance.setVehicleModel(autoInsurance.getVehicleModel());
        autoInsurance.setVehicleYear(autoInsurance.getVehicleYear());
        autoInsurance.setDrivingExperience(autoInsurance.getDrivingExperience());
        autoInsurance.setRecentAccident(autoInsurance.getRecentAccident());
        autoInsurance.setVehicleUsage(autoInsurance.getVehicleUsage());
        return autoInsuranceRepository.save(autoInsurance);
    }


    @Async
    public FireInsurance createFire(SiteUser siteUser, FireJoinDTO fireJoinDTO) {
        FireInsurance fireInsurance = new FireInsurance();
        fireInsurance = create(siteUser, fireJoinDTO, "화재보험", fireInsurance);
        fireInsurance.setPropertyAddress(fireJoinDTO.getPropertyAddress());
        fireInsurance.setBuildingType(fireInsurance.getBuildingType());
        fireInsurance.setBuildingYear(fireJoinDTO.getBuildingYear());
        fireInsurance.setPreviousFire(fireJoinDTO.getPreviousFire());
        return fireInsuranceRepository.save(fireInsurance);
    }


    @Transactional
    public void startInsurance(SiteUser siteUser, int id) {
        Insurance insurance = insuranceRepository.findBySiteUserAndId(siteUser,id)
                .orElseThrow(()->new DataNotFoundException("데이터가 존재하지 않습니다."));
        insurance.setStatus("active");
        insuranceRepository.save(insurance);
    }

    @Transactional
    public void deleteInsurance(SiteUser siteUser, int id) {
        Insurance insurance = insuranceRepository.findBySiteUserAndId(siteUser,id)
                .orElseThrow(()->new DataNotFoundException("데이터가 존재하지 않습니다."));
        insuranceRepository.delete(insurance);
    }


    private int calculateHealthRiskScore(HealthJoinDTO healthJoinDTO, int age) {
        int riskScore = 0;

        if (age > 50) {
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

    private int calculateFireRiskScore(FireJoinDTO fireJoinDTO) {
        int riskScore = 0;
        int currentYear = LocalDate.now().getYear();
        int buildingAge = currentYear - fireJoinDTO.getBuildingYear();

        // 건물 연식에 따른 리스크 점수
        if (buildingAge > 40) { // 40년 이상된 건물
            riskScore += 20;
        } else if (buildingAge > 20) { // 20~40년된 건물
            riskScore += 10;
        }

        // 과거 화재 여부
        if (Boolean.TRUE.equals(fireJoinDTO.getPreviousFire())) {
            riskScore += 30; // 과거 화재 경험이 있으면 큰 리스크로 간주
        }

        // 건물 내 평균 인원
        if (fireJoinDTO.getOccupants() > 10) {
            riskScore += 15; // 사람이 많을수록 리스크 증가
        } else if (fireJoinDTO.getOccupants() > 5) {
            riskScore += 10;
        }

        // 건물 유형
        if ("상업시설".equals(fireJoinDTO.getBuildingType())) {
            riskScore += 20; // 상업시설은 주택보다 리스크가 높음
        } else if ("단독주택".equals(fireJoinDTO.getBuildingType())) {
            riskScore += 10; // 단독주택도 일정 리스크 있음
        }

        return Math.min(Math.max(riskScore, 0), 100); // 점수 0~100으로 제한
    }

    private int calculateAutoRiskScore(AutoJoinDTO autoJoinDTO, int age) {
        int riskScore = 0;
        int currentYear = LocalDate.now().getYear();
        int vehicleAge = currentYear - autoJoinDTO.getVehicleYear();

        // 차량 연식에 따른 리스크 점수
        if (vehicleAge > 20) { // 20년 이상된 차량
            riskScore += 15;
        } else if (vehicleAge > 10) { // 10~20년된 차량
            riskScore += 10;
        }

        // 운전 경력
        if (autoJoinDTO.getDrivingExperience() < 2) {
            riskScore += 25; // 초보 운전자는 높은 리스크
        } else if (autoJoinDTO.getDrivingExperience() < 5) {
            riskScore += 15;
        }

        // 운전자 나이
        if (age >= 18 && age <= 24) { // 초보 연령대
            riskScore += 20;
        } else if (age >= 25 && age <= 34) {
            riskScore += 10;
        } else if (age >= 65) { // 고령 운전자
            riskScore += 15;
        }

        // 최근 사고 여부
        if (Boolean.TRUE.equals(autoJoinDTO.getRecentAccident())) {
            riskScore += 20;
        }

        // 월 평균 주행 거리
        if (autoJoinDTO.getMonthlyMileage() > 2000) {
            riskScore += 15;
        } else if (autoJoinDTO.getMonthlyMileage() > 1000) {
            riskScore += 10;
        }

        // 차량 용도
        if ("영업용".equals(autoJoinDTO.getVehicleUsage())) {
            riskScore += 20;
        } else if ("출퇴근".equals(autoJoinDTO.getVehicleUsage())) {
            riskScore += 15;
        }

        return Math.min(Math.max(riskScore, 0), 130); // 점수 0~130으로 제한
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











