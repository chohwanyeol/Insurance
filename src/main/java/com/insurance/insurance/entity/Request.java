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
    private LocalDateTime request_date;     //요청일
    private LocalDateTime payment_date;     //지급일
    private String status;     //[note: "pending, approved, rejected"] //상태
    private String description;     //설명

    public Request(Insurance insurance, String claimType, String content, Integer price, LocalDateTime request_date, String status) {
        this.insurance = insurance;
        this.type = claimType;
        this.content = content;
        this.price = price;
        this.request_date = request_date;
        this.status = status;
    }

}
