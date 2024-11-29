package com.insurance.insurance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class AutoDTO {

    @NotBlank(message = "은행을 입력해주세요.")
    private String bank;

    @NotNull(message = "계좌번호를 입력해주세요.")
    private String account;

    @NotNull(message = "차량번호를 입력해주세요.")
    private String vehicle_number;

    @NotNull(message = "사고이력을 입력해주세요.")
    private String accident_history;
}
