package com.insurance.insurance.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class HealthRenewDTO {
    @NotNull(message = "흡연 여부를 체크해주세요")
    private Boolean smoke;

    @NotNull(message = "음주 횟수를 입력해주세요")
    private Integer drink;

    @NotNull(message = "운동 시간을 입력해주세요")
    private Integer exercise;


    @NotNull(message = "가족력 체크란을 확인해주세요.")
    private List<String> family;

    @NotNull(message = "기존 병력 체크란을 확인해주세요.")
    private List<String> pre_existing_conditions;


    @NotNull(message = "수술 여부를 체크해주세요.")
    private Boolean surgery;
}
