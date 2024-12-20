package com.insurance.insurance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private SiteUser siteUser;

    @ManyToOne
    private Product product;        //상품정보

    @ManyToOne
    private RiskRank riskRank;      //리스크등급

    @OneToMany
    private List<Transaction> transaction;      //지급내역

    private Integer riskScore;      //리스크점수
    private Integer price;      //점수
    private String bank;        //은행
    private String bankAccount;     //계좌
    private LocalDate startDate;    //시작일
    private LocalDate endDate;      //만료일
    private String status;       //active,expired,pending

    public Insurance(SiteUser siteUser, Product product, Integer riskScore, RiskRank riskRank,
                      Integer price, String bank, String bankAccount, LocalDate startDate, LocalDate endDate, String status) {
        this.siteUser = siteUser;
        this.product = product;
        this.riskScore = riskScore;
        this.riskRank = riskRank;
        this.price = price;
        this.bank = bank;
        this.bankAccount = bankAccount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

}
