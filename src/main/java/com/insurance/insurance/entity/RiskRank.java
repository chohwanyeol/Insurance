package com.insurance.insurance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class RiskRank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String name;

    @ManyToOne
    private Product product;        //상품

    private Integer deductible_rate;        //보험비 지급률
    private Integer coverage_limit;     //보장한도
    private String description;     //설명
    private Double price_rate;      //가격배율

    public RiskRank(String name, Product product, Integer deductible_rate, Integer coverage_limit,
                    String description, Double price_rate){
        this.name = name;
        this.product = product;
        this.deductible_rate = deductible_rate;
        this.coverage_limit = coverage_limit;
        this.description = description;
        this.price_rate = price_rate;
    }
}
