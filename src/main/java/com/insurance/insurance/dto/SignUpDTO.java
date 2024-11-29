package com.insurance.insurance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@RequiredArgsConstructor
public class SignUpDTO {

    @NotBlank(message = "사용자 이름이 필요합니다.")
    @Size(min = 3, max = 50, message = "사용자 이름은 3자 이상, 50자 이하여야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호가 필요합니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인이 필요합니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String checkPassword;

    @NotBlank(message = "이메일 주소가 필요합니다.")
    @Email(message = "유효한 이메일 주소여야 합니다.")
    private String email;

    @NotBlank(message = "이름이 필요합니다.")
    private String name;

    @NotNull(message = "생년월일이 필요합니다.")
    private LocalDate birthDay;

    public boolean passwordMatching() {
        return password.equals(checkPassword);
    }
}
