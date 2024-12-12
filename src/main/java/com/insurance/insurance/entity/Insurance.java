package com.insurance.insurance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private SiteUser siteUser;

    @ManyToOne
    private Product product;

    private Integer riskScore;
    @OneToOne
    private RiskRank riskRank;


    private Integer price;
    private String bank;
    private String bankAccount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Insurance(SiteUser siteUser, Product product, Integer riskScore, RiskRank riskRank,
                      Integer price, String bank, String bankAccount, LocalDateTime startDate, LocalDateTime endDate) {
        this.siteUser = siteUser;
        this.product = product;
        this.riskScore = riskScore;
        this.riskRank = riskRank;
        this.price = price;
        this.bank = bank;
        this.bankAccount = bankAccount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
