package com.insurance.insurance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor

public class FireInsurance extends Insurance{
    private String propertyAddress;     //재산 주소

    private String buildingType;        //건물 유형

    private Integer buildingYear;       //건축 년도

    private Boolean previousFire;       //이전 화재 여부
}
