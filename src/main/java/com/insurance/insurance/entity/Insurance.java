package com.insurance.insurance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

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
    private Integer price;
    private String bank;
    private String BankAccount;
    private Integer deductible_rate;
    private Integer coverage_limit;
    private Timestamp startDate;
    private Timestamp endDate;
}
