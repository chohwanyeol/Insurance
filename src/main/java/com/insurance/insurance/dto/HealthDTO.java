package com.insurance.insurance.dto;

import com.insurance.insurance.entity.AutoInsurance;
import com.insurance.insurance.entity.HealthInsurance;
import com.insurance.insurance.entity.Insurance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HealthDTO extends InsuranceDTO {
    private Integer healthId;

    public void EntityToDTO(HealthInsurance healthInsurance) {
        super.EntityToDTO(healthInsurance);
        this.healthId = healthInsurance.getId();
    }
}
