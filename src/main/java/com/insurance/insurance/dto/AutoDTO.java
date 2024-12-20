package com.insurance.insurance.dto;

import com.insurance.insurance.entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AutoDTO extends InsuranceDTO{
    private Integer autoId;
    private String vehicleNumber;    // 차량 번호
    private String vehicleModel;     // 차량 모델
    private Integer vehicleYear;     // 차량 연식
    private String vehicleUsage;     // 차량 용도 (출퇴근, 영업용 등)


    public void EntityToDTO(AutoInsurance autoInsurance) {
        super.EntityToDTO(autoInsurance);
        this.autoId = autoInsurance.getId();
        this.vehicleNumber = autoInsurance.getVehicleNumber();
        this.vehicleModel = autoInsurance.getVehicleModel();
        this.vehicleYear = autoInsurance.getVehicleYear();
        this.vehicleUsage = autoInsurance.getVehicleUsage();
    }
}
