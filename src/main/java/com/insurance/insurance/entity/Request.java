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
    private String type;        //요청타입
    private String content;     //요청내용
    private Integer price;      //비용
    private LocalDateTime requestDate;     //요청일
    @OneToOne
    private Transaction transaction;
    private String status;     //[note: "pending, approved, rejected"] //상태
    private String description;     //설명


    public Request(SiteUser siteUser,Insurance insurance, String claimType, String content, Integer price, LocalDateTime requestDate, String status) {
        this.siteUser = siteUser;
        this.insurance = insurance;
        this.type = claimType;
        this.content = content;
        this.price = price;
        this.requestDate = requestDate;
        this.status = status;
    }

}
