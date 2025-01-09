package com.insurance.insurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FireRenewDTO {

    @NotNull(message = "과거 화재 여부를 입력해주세요.")
    private Boolean previousFire;

    @NotNull(message = "건물 내 평균 인원을 입력해주세요.")
    private Integer occupants;

    @NotNull(message = "건물 주소를 입력해주세요.")
    private String propertyAddress;

    @NotNull(message = "건물 상세 주소를 입력해주세요.")
    private String propertyDetailAddress;
}
