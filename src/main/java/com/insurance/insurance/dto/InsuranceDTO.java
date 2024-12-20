package com.insurance.insurance.dto;


import com.insurance.insurance.entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class InsuranceDTO {
    private Integer insuranceId;         // 보험 ID
    private String productName;          // 보험 이름
    private String productDescription;   // 보험 설명
    private String userName;             // 가입자 이름
    private String userBirthday;         // 가입자 생년월일
    private String bank;                 // 은행 이름
    private String bankAccount;          // 계좌 번호
    private String riskRankName;         // 리스크 등급 이름
    private String riskRankDescription;  // 리스크 등급 설명
    private Integer price;               // 보험료
    private Integer deductibleRate;      // 공제율
    private Integer coverageLimit;       // 보장 한도
    private LocalDate startDate;         // 시작일
    private LocalDate endDate;           // 종료일


    public void EntityToDTO(Insurance insurance) {
        RiskRank riskRank = insurance.getRiskRank();
        Product product = insurance.getProduct();
        UserInfo userInfo = insurance.getSiteUser().getUserInfo();
        this.insuranceId = insurance.getId();
        this.productName = product.getName();
        this.productDescription = product.getDescription();
        this.bank = insurance.getBank();
        this.bankAccount = insurance.getBankAccount();
        this.riskRankName = riskRank.getName();
        this.riskRankDescription = riskRank.getDescription();
        this.price = insurance.getPrice();
        this.deductibleRate = riskRank.getDeductible_rate();
        this.coverageLimit = riskRank.getCoverage_limit() * insurance.getPrice();
        this.startDate = insurance.getStartDate();
        this.endDate = insurance.getEndDate();
        this.userName = userInfo.getName();
        this.userBirthday = userInfo.getBirthDay().toString();
    }

}
