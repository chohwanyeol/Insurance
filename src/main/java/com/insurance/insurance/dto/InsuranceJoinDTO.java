package com.insurance.insurance.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public abstract class InsuranceJoinDTO {
    @NotBlank(message = "은행을 입력해주세요.")
    private String bank;

    @NotNull(message = "계좌번호를 입력해주세요.")
    private String account;

    @NotNull(message = "가입년수를 선택해주세요.")
    private Integer duration;
}
