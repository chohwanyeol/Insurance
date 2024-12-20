package com.insurance.insurance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class HealthInsurance extends Insurance{

    private String family;      //가족력

    private String preExistingConditions;       //이전 병력

}
