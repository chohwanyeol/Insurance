package com.insurance.insurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class LoginDTO {
    @NotBlank(message = "사용자 이름이 필요합니다.")
    @Size(min = 3, max = 50, message = "사용자 이름은 3자 이상, 50자 이하여야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호가 필요합니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String password;
}
