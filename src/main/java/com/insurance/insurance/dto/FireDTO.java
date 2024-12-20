package com.insurance.insurance.dto;

import com.insurance.insurance.entity.FireInsurance;
import com.insurance.insurance.entity.HealthInsurance;
import com.insurance.insurance.entity.Insurance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class FireDTO extends InsuranceDTO{
    private Integer fireId;
    private String propertyAddress;     //재산 주소
    private String buildingType;        //건물 유형
    private Integer buildingYear;       //건축 년도

    public void EntityToDTO(FireInsurance fireInsurance) {
        super.EntityToDTO(fireInsurance);
        this.fireId = fireInsurance.getId();
        this.propertyAddress = fireInsurance.getPropertyAddress();
        this.buildingType = fireInsurance.getBuildingType();
    }
}
