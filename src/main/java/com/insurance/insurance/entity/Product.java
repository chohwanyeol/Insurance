package com.insurance.insurance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;        //상품이름

    private String description;     //설명

    private Integer price;

    private LocalDate create_date;

    private LocalDate update_date;

    public Product(String name, String description, Integer price,LocalDate create_date){
        this.name = name;
        this.description = description;
        this.price = price;
        this.create_date = create_date;
    }
}