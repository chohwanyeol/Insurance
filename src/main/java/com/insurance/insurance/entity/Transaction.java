package com.insurance.insurance.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Transaction {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @ManyToOne
    private SiteUser siteUser;

    @ManyToOne
    private Insurance insurance;

    private Integer price;      //금액

    private LocalDateTime dateTime;

    private String status;      //[note: "pending, approved, rejected"]

    public Transaction(SiteUser siteUser, Insurance insurance, Integer price, LocalDateTime dateTime, String status) {
    }
}
