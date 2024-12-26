package com.insurance.insurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutoRenewDTO {
    @NotNull(message = "최근 사고 여부를 입력해주세요.")
    private Boolean recentAccident;

    @NotNull(message = "한 달 평균 주행 거리를 입력해주세요.")
    private Integer monthlyMileage;

    @NotBlank(message = "차량 용도를 입력해주세요.")
    private String vehicleUsage;
}
