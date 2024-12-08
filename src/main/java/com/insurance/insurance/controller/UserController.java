package com.insurance.insurance.controller;

import com.insurance.insurance.dto.LoginDTO;
import com.insurance.insurance.dto.SignUpDTO;
import com.insurance.insurance.entity.SiteUser;
import com.insurance.insurance.entity.UserInfo;
import com.insurance.insurance.jwt.JwtTokenProvider;
import com.insurance.insurance.service.UserInfoService;
import com.insurance.insurance.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;
    private final UserInfoService userInfoService;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/user/login")
    public String login(){
        return "login";
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );

        String token = jwtTokenProvider.generateToken(loginDto.getUsername(), "ROLE_USER");
        return ResponseEntity.ok(Map.of("token", token));
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
        SiteUser siteUser = userService.createByDTO(signUpDTO);
        UserInfo userInfo = userInfoService.createUserInfoByDTO(signUpDTO);
        return ResponseEntity.ok("회원가입 성공");
    }
}
