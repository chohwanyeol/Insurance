package com.insurance.insurance.dto;

import com.insurance.insurance.entity.HealthInsurance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HealthInsuranceDTO extends InsuranceDTO {
    private Integer healthId;

    public void EntityToDTO(HealthInsurance healthInsurance) {
        super.EntityToDTO(healthInsurance);
        this.healthId = healthInsurance.getId();
    }
}
