package com.insurance.insurance.controller;

import com.insurance.insurance.dto.SignUpDTO;
import com.insurance.insurance.entity.SiteUser;
import com.insurance.insurance.entity.UserInfo;
import com.insurance.insurance.service.UserInfoService;
import com.insurance.insurance.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final UserInfoService userInfoService;
    @GetMapping("/user/login")
    public String login(){
        return "login";
    }

    @GetMapping("/user/signup")
    public String signup(){
        return "signup";
    }

    @PostMapping("/user/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpDTO signUpDTO){
        if (signUpDTO.passwordMatching()) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }
        SiteUser siteUser = userService.createUserByDTO(signUpDTO);
        UserInfo userInfo = userInfoService.createUserInfoByDTO(signUpDTO);
        return ResponseEntity.ok("회원가입 성공");
    }
}
