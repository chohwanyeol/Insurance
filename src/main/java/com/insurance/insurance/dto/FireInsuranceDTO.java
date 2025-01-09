package com.insurance.insurance.dto;

import com.insurance.insurance.entity.FireInsurance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class FireInsuranceDTO extends InsuranceDTO{
    private String propertyAddress;     //재산 주소
    private String propertyDetailAddress; // 상세 주소
    private String buildingType;        //건물 유형
    private Integer buildingYear;       //건축 년도

    public void EntityToDTO(FireInsurance fireInsurance) {
        super.EntityToDTO(fireInsurance);
        this.propertyAddress = fireInsurance.getPropertyAddress();
        this.propertyDetailAddress = fireInsurance.getPropertyDetailAddress();
        this.buildingType = fireInsurance.getBuildingType();
        this.buildingYear = fireInsurance.getBuildingYear();

    }
}
