package com.insurance.insurance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Request {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @ManyToOne
    private SiteUser siteUser;

    @ManyToOne
    private Insurance insurance;
    private String claimType;
    private String content;
    private Integer price;
    private LocalDateTime request_date;
    private LocalDateTime payment_date;
    private String status;

    public Request(Insurance insurance, String claimType, String content, Integer price, LocalDateTime request_date, String status) {
        this.insurance = insurance;
        this.claimType = claimType;
        this.content = content;
        this.price = price;
        this.request_date = request_date;
        this.status = status;
    }

}
