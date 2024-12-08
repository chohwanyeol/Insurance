package com.insurance.insurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class HealthJoinDTO {
    @NotBlank(message = "은행을 입력해주세요.")
    private String bank;

    @NotNull(message = "계좌번호를 입력해주세요.")
    private String account;

    @NotNull(message = "기존병력을 입력해주세요.")
    private String pre_existing_conditions;

}
