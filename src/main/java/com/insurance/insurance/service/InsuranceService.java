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
        Product product = productService.getByName("auto");
        List<AutoInsurance> autoInsuranceList = autoInsuranceRepository.findAllBySiteUser(siteUser)
                .orElseThrow(() -> new DataNotFoundException("해당 가입내용이 존재하지 않습니다."));

        return autoInsuranceList;
    }

    public AutoInsurance getAutoBySiteUserAndId(SiteUser siteUser, Integer id) {
        return autoInsuranceRepository.findBySiteUserAndId(siteUser,id)
                .orElseThrow(() -> new DataNotFoundException("해당 데이터가 존재하지 않습니다."));
    }

    public List<HealthInsurance> getHealthBySiteUser(SiteUser siteUser) {
        List<HealthInsurance> healthInsuranceList = healthInsuranceRepository.findAllBySiteUser(siteUser)
                .orElseThrow(() -> new DataNotFoundException("해당 가입내용이 존재하지 않습니다."));
        return healthInsuranceList;
    }

    public List<RenewableInsurance> getRenewableBySiteUser(SiteUser siteUser) {
        return renewableInsuranceRepository.findBySiteUser(siteUser);
    }

    public RenewableInsurance getRenewableBySiteUserAndId(SiteUser siteUser, Integer id) {
        return renewableInsuranceRepository.findBySiteUserAndId(siteUser, id)
                .orElseThrow(()-> new DataNotFoundException("데이터가 존재하지 않습니다."));
    }

    @Transactional
    private <T extends Insurance> T create(SiteUser siteUser, InsuranceJoinDTO insuranceJoinDTO, String type, T insurance) {
        UserInfo userInfo = siteUser.getUserInfo();
        int age = Period.between(userInfo.getBirthDay(), LocalDate.now()).getYears();

        int riskScore = 0;
        String rank = "";
        switch (type) {
            case "health":
                riskScore = calculateHealthRiskScore((HealthJoinDTO) insuranceJoinDTO, age);
                rank = getCoveragePlan(riskScore);
                break;
            case "auto":
                riskScore = calculateAutoRiskScore((AutoJoinDTO) insuranceJoinDTO, age);
                rank = getCoveragePlan(riskScore);
                break;
            case "fire":
                riskScore = calculateFireRiskScore((FireJoinDTO) insuranceJoinDTO);
                rank = getCoveragePlan(riskScore);
                break;
        }

        Product product = productService.getByName(type);
        RiskRank riskRank = riskRankService.getByNameAndProduct(rank, product);

        int price = (int) (riskRank.getPrice_rate()/100 * product.getPrice());
        String bank = insuranceJoinDTO.getBank();
        String bankAccount = insuranceJoinDTO.getAccount();
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusYears(insuranceJoinDTO.getDuration()).minusDays(1);

        // 부모 클래스 필드 설정
        insurance.setSiteUser(siteUser);
        insurance.setProduct(product);
        insurance.setRiskRank(riskRank);
        insurance.setRiskScore(riskScore);
        insurance.setPrice(price);
        insurance.setStatus("pending");
        insurance.setStartDate(startDate);
        insurance.setEndDate(endDate);
        insurance.setBank(bank);
        insurance.setBankAccount(bankAccount);

        return insuranceRepository.save(insurance);
    }

    @Transactional
    public Insurance cancel(Insurance insurance) {
        //insurance.setStatus("cancelled");
        LocalDate localDate = insurance.getEndDate();
        LocalDate currentDate = LocalDate.now();
        int day = localDate.getDayOfMonth();
        LocalDate targetDate = currentDate.withDayOfMonth(day);
        if (targetDate.compareTo(currentDate) <0){
            targetDate = targetDate.plusMonths(1);
        }
        insurance.setEndDate(targetDate);
        return insuranceRepository.save(insurance);
    }

    @Transactional
    public HealthInsurance createHealth(SiteUser siteUser, HealthJoinDTO healthJoinDTO) {
        HealthInsurance healthInsurance = new HealthInsurance();
        healthInsurance = create(siteUser, healthJoinDTO, "health",healthInsurance);
        healthInsurance.setFamily(String.join(", ", healthJoinDTO.getFamily()));
        healthInsurance.setPreExistingConditions(String.join(", ", healthJoinDTO.getPre_existing_conditions()));
        return healthInsuranceRepository.save(healthInsurance);
    }

    @Transactional
    public HealthInsurance updateHealth(SiteUser siteUser, Integer id, HealthRenewDTO healthRenewDTO){
        FireInsurance fireInsurance = fireInsuranceRepository.findBySiteUserAndId(siteUser,id)
                .orElseThrow(()->new DataNotFoundException("데이터가 존재하지 않습니다."));

        HealthInsurance healthInsurance = healthInsuranceRepository.findBySiteUserAndId(siteUser,id)
                .orElseThrow(()->new DataNotFoundException("데이터가 존재하지 않습니다."));
        Product product = healthInsurance.getProduct();
        HealthJoinDTO healthJoinDTO = new HealthJoinDTO();
        healthJoinDTO.setDrink(healthRenewDTO.getDrink());
        healthJoinDTO.setExercise(healthRenewDTO.getExercise());
        healthJoinDTO.setSmoke(healthRenewDTO.getSmoke());
        healthJoinDTO.setFamily(healthRenewDTO.getFamily());
        healthJoinDTO.setSurgery(healthRenewDTO.getSurgery());
        healthJoinDTO.setPre_existing_conditions(healthRenewDTO.getPre_existing_conditions());
        int age = Period.between(siteUser.getUserInfo().getBirthDay(), LocalDate.now()).getYears();
        int riskScore = calculateHealthRiskScore(healthJoinDTO, age);
        RiskRank riskRank = riskRankService.getByNameAndProduct(getCoveragePlan(riskScore),product);
        int price = (int) (riskRank.getPrice_rate() * product.getPrice());
        healthInsurance.setPrice(price);
        healthInsurance.setRiskScore(riskScore);
        healthInsurance.setRiskRank(riskRank);
        healthInsurance.setFamily(String.join(", ", healthJoinDTO.getFamily()));
        healthInsurance.setPreExistingConditions(String.join(", ", healthJoinDTO.getPre_existing_conditions()));
        return healthInsuranceRepository.save(healthInsurance);
    }

    @Transactional
    public AutoInsurance createAuto(SiteUser siteUser, AutoJoinDTO autoJoinDTO) {
        AutoInsurance autoInsurance = new AutoInsurance();
        autoInsurance = create(siteUser, autoJoinDTO, "auto", autoInsurance);
        autoInsurance.setVehicleNumber(autoJoinDTO.getVehicleNumber());
        autoInsurance.setVehicleModel(autoJoinDTO.getVehicleModel());
        autoInsurance.setVehicleYear(autoJoinDTO.getVehicleYear());
        autoInsurance.setDrivingStartDate(autoJoinDTO.getDrivingStartDate());
        autoInsurance.setRecentAccident(autoJoinDTO.getRecentAccident());
        autoInsurance.setVehicleUsage(autoJoinDTO.getVehicleUsage());
        return autoInsuranceRepository.save(autoInsurance);
    }

    @Transactional
    public AutoInsurance updateAuto(SiteUser siteUser, Integer id, AutoRenewDTO autoRenewDTO){
        AutoInsurance autoInsurance = autoInsuranceRepository.findBySiteUserAndId(siteUser,id)
                .orElseThrow(()->new DataNotFoundException("데이터가 존재하지 않습니다."));
        Product product = autoInsurance.getProduct();
        AutoJoinDTO autoJoinDTO = new AutoJoinDTO();

        autoJoinDTO.setMonthlyMileage(autoRenewDTO.getMonthlyMileage());
        autoJoinDTO.setRecentAccident(autoRenewDTO.getRecentAccident());
        autoJoinDTO.setVehicleUsage(autoRenewDTO.getVehicleUsage());
        autoJoinDTO.setDrivingStartDate(autoInsurance.getDrivingStartDate());
        autoJoinDTO.setVehicleYear(autoInsurance.getVehicleYear());
        int age = Period.between(siteUser.getUserInfo().getBirthDay(), LocalDate.now()).getYears();
        int riskScore = calculateAutoRiskScore(autoJoinDTO, age);
        RiskRank riskRank = riskRankService.getByNameAndProduct(getCoveragePlan(riskScore),product);
        int price = (int) (riskRank.getPrice_rate() * product.getPrice());

        autoInsurance.setVehicleUsage(autoJoinDTO.getVehicleUsage());
        autoInsurance.setVehicleYear(autoJoinDTO.getVehicleYear());
        autoInsurance.setDrivingStartDate(autoJoinDTO.getDrivingStartDate());
        autoInsurance.setRecentAccident(autoJoinDTO.getRecentAccident());
        autoInsurance.setRiskScore(riskScore);
        autoInsurance.setRiskRank(riskRank);
        return autoInsuranceRepository.save(autoInsurance);
    }

    @Transactional
    public FireInsurance createFire(SiteUser siteUser, FireJoinDTO fireJoinDTO) {
        FireInsurance fireInsurance = new FireInsurance();
        fireInsurance = create(siteUser, fireJoinDTO, "fire", fireInsurance);
        fireInsurance.setPropertyAddress(fireJoinDTO.getPropertyAddress());
        fireInsurance.setPropertyDetailAddress(fireJoinDTO.getPropertyDetailAddress());
        fireInsurance.setBuildingType(fireJoinDTO.getBuildingType());
        fireInsurance.setBuildingYear(fireJoinDTO.getBuildingYear());
        fireInsurance.setPreviousFire(fireJoinDTO.getPreviousFire());
        return fireInsuranceRepository.save(fireInsurance);
    }

    @Transactional
    public FireInsurance updateFire(SiteUser siteUser, Integer id, FireRenewDTO fireRenewDTO) {
        FireInsurance fireInsurance = fireInsuranceRepository.findBySiteUserAndId(siteUser,id)
                .orElseThrow(()->new DataNotFoundException("데이터가 존재하지 않습니다."));

        Product product = fireInsurance.getProduct();
        FireJoinDTO fireJoinDTO = new FireJoinDTO();
        fireJoinDTO.setPreviousFire(fireRenewDTO.getPreviousFire());
        fireJoinDTO.setBuildingYear(fireInsurance.getBuildingYear());
        fireJoinDTO.setBuildingType(fireInsurance.getBuildingType());
        fireJoinDTO.setOccupants(fireRenewDTO.getOccupants());
        int riskScore = calculateFireRiskScore(fireJoinDTO);
        RiskRank riskRank = riskRankService.getByNameAndProduct(getCoveragePlan(riskScore),product);
        int price = (int) (riskRank.getPrice_rate() * product.getPrice());
        fireInsurance.setPrice(price);
        fireInsurance.setPreviousFire(fireJoinDTO.getPreviousFire());
        fireInsurance.setRiskScore(riskScore);
        fireInsurance.setRiskRank(riskRank);
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
        LocalDate drivingStartDate = autoJoinDTO.getDrivingStartDate(); // 운전 시작 날짜
        LocalDate today = LocalDate.now();
        // 시작 날짜와 오늘의 차이를 계산
        Period period = Period.between(drivingStartDate, today);
        // 정확한 운전 경력 계산 (월과 일을 포함한 연 단위)
        int drivingExperience = period.getYears();


        // 차량 연식에 따른 리스크 점수
        if (vehicleAge > 20) { // 20년 이상된 차량
            riskScore += 15;
        } else if (vehicleAge > 10) { // 10~20년된 차량
            riskScore += 10;
        }

        // 운전 경력
        if (drivingExperience < 2) {
            riskScore += 25; // 초보 운전자는 높은 리스크
        } else if (drivingExperience < 5) {
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

    @Transactional
    public void deleteAllByStatus(String status) { insuranceRepository.deleteAllByStatus(status);
    }

    public AutoInsurance getAutoBySiteUserAndVehicleNumber(SiteUser siteUser, String vehicleNumber) {
        return autoInsuranceRepository.findBySiteUserAndVehicleNumber(siteUser,vehicleNumber)
                .orElseThrow(()-> new DataNotFoundException("데이터가 존재하지 않습니다."));
    }

    public FireInsurance getFireBySiteUserAndPropertyAddressAndPropertyDetailAddressAndBuildingType(SiteUser siteUser, String propertyAddress, String propertyDetailAddress, String buildingType) {
        return fireInsuranceRepository.findBySiteUserAndPropertyAddressAndPropertyDetailAddressAndBuildingType(siteUser,propertyAddress,propertyDetailAddress,buildingType)
                .orElseThrow(()->new DataNotFoundException("데이터가 존재하지 않습니다."));
    }


    @Transactional
    public List<RenewableInsurance> getDontRenew() {
        LocalDate now = LocalDate.now();
        LocalDate targetDate = now.minusMonths(2); // 현재 날짜에서 한 달 후 계산
        int targetYear = targetDate.getYear();
        int targetMonth = targetDate.getMonthValue();
        int targetDay = targetDate.getDayOfMonth();
        List<RenewableInsurance> renewableInsuranceList = renewableInsuranceRepository.findAllDontRenew(targetYear,targetMonth,targetDay);
        return renewableInsuranceList;
    }

    @Transactional
    public void setDontRenew(List<RenewableInsurance> renewableInsuranceList) {
        List<Insurance> insuranceList = renewableInsuranceList.stream()
                .map(renewableInsurance -> {
                    Insurance insurance = renewableInsurance.getInsurance();
                    Product product = insurance.getProduct();
                    RiskRank riskRank = riskRankService.getByNameAndProduct("Emergency Only", product);
                    insurance.setRiskRank(riskRank);
                    insurance.setRiskScore(100);
                    int price = (int) (riskRank.getPrice_rate() * product.getPrice());
                    insurance.setPrice(price);
                    return insurance;
                })
                .toList();

        insuranceRepository.saveAll(insuranceList);
        renewableInsuranceRepository.deleteAll(renewableInsuranceList);
    }



    @Transactional
    public List<Insurance> getInsuranceRenewable() {
        LocalDate now = LocalDate.now();
        LocalDate targetDate = now.plusMonths(1); // 현재 날짜에서 한 달 후 계산
        int targetYear = targetDate.getYear();
        int targetMonth = targetDate.getMonthValue();
        int targetDay = targetDate.getDayOfMonth();
        List<Insurance> insuranceList = insuranceRepository.findAllRenewable(targetYear,targetMonth,targetDay);
        return insuranceList;
    }

    @Transactional
    public void setInsuranceRenewable(List<Insurance> insuranceList) {
        List<RenewableInsurance> renewableInsuranceList = insuranceList.stream()
                .map(insurance -> {
                    RenewableInsurance renewableInsurance = new RenewableInsurance();
                    renewableInsurance.setInsurance(insurance);
                    renewableInsurance.setSiteUser(insurance.getSiteUser());
                    renewableInsurance.setCreateDate(LocalDate.now());
                    return renewableInsurance;
                })
                .toList();
        renewableInsuranceRepository.saveAll(renewableInsuranceList);

    }




    @Transactional
    public void setInsuranceExpired() {
        LocalDate now = LocalDate.now();
        List<Insurance> insuranceList =
                insuranceRepository.findAllByEndDate(now)
                        .stream().map(insurance -> {
                            insurance.setStatus("expired");
                            return insurance;
                        }).toList();

        insuranceRepository.saveAll(insuranceList);

    }

    public HealthInsurance getHealthBySiteUserAndId(SiteUser siteUser, int id) {
        return healthInsuranceRepository.findBySiteUserAndId(siteUser,id)
                .orElseThrow(
                        ()-> new DataNotFoundException("데이터가 존재하지 않습니다")
                );
    }

    public List<Insurance> getBySiteUserAndStatus(SiteUser siteUser, String status) {
        return insuranceRepository.findAllBySiteUserAndStatus(siteUser,status);

    }

    public List<FireInsurance> getFireBySiteUserAndStatus(SiteUser siteUser, String status) {
        return fireInsuranceRepository.findAllBySiteUserAndStatus(siteUser,status);
    }

    public List<AutoInsurance> getAutoBySiteUserAndStatus(SiteUser siteUser, String status) {
        return autoInsuranceRepository.findAllBySiteUserAndStatus(siteUser,status);
    }

    public FireInsurance getFirstFireBySiteUserAndStatus(SiteUser siteUser, String status) {
        return fireInsuranceRepository.findFirstBySiteUserAndStatus(siteUser,status)
                .orElseThrow(
                        ()-> new DataNotFoundException("데이터가 존재하지 않습니다")
                );
    }

    public AutoInsurance getFirstAutoBySiteUserAndStatus(SiteUser siteUser, String status) {
        return autoInsuranceRepository.findFirstBySiteUserAndStatus(siteUser,status)
                .orElseThrow(
                        ()-> new DataNotFoundException("데이터가 존재하지 않습니다")
                );
    }


    public HealthInsurance getHealthBySiteUserAndStatus(SiteUser siteUser, String status) {
        return healthInsuranceRepository.findBySiteUserAndStatus(siteUser,status)
                .orElseThrow(
                        ()-> new DataNotFoundException("데이터가 존재하지 않습니다")
                );
    }

    @Transactional
    public void deleteRenewable(RenewableInsurance renewableInsurance) {
        renewableInsuranceRepository.delete(renewableInsurance);
    }


    @Transactional
    public Insurance updateBank(Insurance insurance, UpdateBankDTO updateBankDTO) {
        insurance.setBank(updateBankDTO.getBank());
        insurance.setBankAccount(updateBankDTO.getAccount());
        insurance = insuranceRepository.save(insurance);
        return insurance;
    }
}











