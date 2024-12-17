package com.insurance.insurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutoJoinDTO extends InsuranceJoinDTO{

    @NotNull(message = "차량번호를 입력해주세요.")
    private String vehicleNumber;

    @NotNull(message = "차량 모델을 입력해주세요.")
    private String vehicleModel;

    @NotNull(message = "차량 연식을 입력해주세요.")
    private Integer vehicleYear;

    @NotNull(message = "운전 경력을 입력해주세요.")
    private Integer drivingExperience;

    @NotNull(message = "최근 사고 여부를 입력해주세요.")
    private Boolean recentAccident;

    @NotNull(message = "한 달 평균 주행 거리를 입력해주세요.")
    private Integer monthlyMileage;

    @NotBlank(message = "차량 용도를 입력해주세요.")
    private String vehicleUsage;
}
