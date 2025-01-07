package com.insurance.insurance.controller;

import com.insurance.insurance.dto.LoginDTO;
import com.insurance.insurance.dto.SignUpDTO;
import com.insurance.insurance.entity.SiteUser;
import com.insurance.insurance.entity.UserInfo;
import com.insurance.insurance.jwt.JwtTokenProvider;
import com.insurance.insurance.repository.UserRepository;
import com.insurance.insurance.service.UserInfoService;
import com.insurance.insurance.service.UserService;
import com.insurance.insurance.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RequiredArgsConstructor
@RestController // @Controller -> @RestController로 변경
@RequestMapping("/user") // 기존 링크 유지
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserInfoService userInfoService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            String token = jwtTokenProvider.generateToken(loginDto.getUsername(), "ROLE_USER");
            return ResponseUtil.createSuccessResponse("token", token);

        } catch (BadCredentialsException e) {
            // 401 Unauthorized 처리
            return ResponseUtil.createErrorResponse(HttpStatus.UNAUTHORIZED, "잘못된 사용자 이름 또는 비밀번호입니다.");
        } catch (AuthenticationException e) {
            // 기타 인증 관련 예외 처리
            return ResponseUtil.createErrorResponse(HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다.");
        } catch (Exception e) {
            // 기타 서버 에러 처리
            return ResponseUtil.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
        }
    }



    // 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpDTO signUpDTO) {
        // 비밀번호 확인
        if (!signUpDTO.passwordMatching()) {
            return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        // UserInfo 및 SiteUser 생성
        UserInfo userInfo = userInfoService.createUserInfoByDTO(signUpDTO);
        userService.createByDTO(signUpDTO, userInfo);

        // 성공 응답
        return ResponseUtil.createSuccessResponse("message", "회원가입 성공");
    }

//    // 사용자 정보 확인 API (JWT를 이용한 인증)
//    @GetMapping("/me")
//    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
//        String username = jwtTokenProvider.getUsernameFromToken(token);
//        SiteUser siteUser = userService.getByUsername(username);
//
//        if (siteUser == null) {
//            return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
//        }
//
//        return ResponseUtil.createSuccessResponse("user", siteUser);
//    }
}
