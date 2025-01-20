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

    @OneToOne
    private Request request;

    private Integer price;      //금액

    private LocalDateTime dateTime;

    private String status;      //[note: "pending, approved, rejected"]

    public Transaction(SiteUser siteUser, Request request, Integer price, LocalDateTime dateTime, String status) {
        this.siteUser = siteUser;
        this.request = request;
        this.price =price;
        this.dateTime = dateTime;
        this.status=status;
    }
}
