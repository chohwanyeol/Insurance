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
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private SiteUser siteUser;

    @ManyToOne
    private Product product;

    @ManyToOne
    private RiskRank riskRank;

    @OneToMany
    private List<Transaction> transaction;

    private Integer riskScore;
    private Integer price;
    private String bank;
    private String bankAccount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;       //active,cancelled,expired

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
