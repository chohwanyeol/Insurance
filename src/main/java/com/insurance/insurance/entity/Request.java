package com.insurance.insurance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
public class Request {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @ManyToOne
    private SiteUser siteUser;

    private Integer request_price;
    private Integer payment_price;
    private Timestamp request_date;
    private Timestamp payment_date;

    private String status;
}
