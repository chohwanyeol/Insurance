package com.insurance.insurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FireJoinDTO {
    @NotBlank(message = "은행을 입력해주세요.")
    private String bank;

    @NotNull(message = "계좌번호를 입력해주세요.")
    private String account;

    @NotNull(message = "재산 주소를 입력해주세요.")
    private String property_address;

    @NotNull(message = "건물 유형을 입력해주세요.")
    private String building_type;
}
