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

    private Integer price;

    private LocalDateTime dateTime;

    private String status;

    public Transaction(SiteUser siteUser, Integer price, LocalDateTime dateTime, String status) {
    }
}
