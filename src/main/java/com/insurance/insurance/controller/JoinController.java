package com.insurance.insurance.controller;

import com.insurance.insurance.dto.InsuranceJoinDTO;
import com.insurance.insurance.entity.AutoInsurance;
import com.insurance.insurance.entity.FireInsurance;
import com.insurance.insurance.entity.HealthInsurance;
import com.insurance.insurance.entity.SiteUser;
import com.insurance.insurance.exception.DataNotFoundException;
import com.insurance.insurance.service.InsuranceService;
import com.insurance.insurance.service.UserService;
import com.insurance.insurance.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import com.insurance.insurance.dto.AutoJoinDTO;
import com.insurance.insurance.dto.FireJoinDTO;
import com.insurance.insurance.dto.HealthJoinDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/join")
@RequiredArgsConstructor
public class JoinController {

    private final InsuranceService insuranceService; // 보험 관련 서비스
    private final UserService userService; // 사용자 관련 서비스

    @PreAuthorize("isAuthenticated")
    @GetMapping("/health")
    public ResponseEntity<?> getHealthProduct(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername(); // 인증된 사용자 이름 가져오기
        SiteUser siteUser = userService.getByUsername(username); // 사용자 정보 조회
        try{
            HealthInsurance healthInsurance = insuranceService.getHealthBySiteUser(siteUser); // 건강보험 가입 여부 확인
            return ResponseUtil.createSuccessResponse("status",false);
        }catch (DataNotFoundException e){
            return ResponseUtil.createSuccessResponse("status",true); // 미가입 상태 반환
        }
    }

    @PreAuthorize("isAuthenticated")
    @PostMapping("/health")
    public ResponseEntity<?> postHealthProduct(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody HealthJoinDTO healthJoinDTO){
        // 비동기 처리 - 건강보험 가입 요청 처리
        /*   ai에게 가입 가능 판별 및 리스크 책정 후 메일로 결과 발송?  */
        String username = userDetails.getUsername(); // 인증된 사용자 이름 가져오기
        SiteUser siteUser = userService.getByUsername(username);
        insuranceService.createHealth(siteUser,healthJoinDTO);
        return ResponseUtil.createSuccessResponse("message","가입 신청 되었습니다.");
    }


    @PreAuthorize("isAuthenticated")
    @GetMapping("/auto")
    public ResponseEntity<?> getAutoProduct(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername(); // 인증된 사용자 이름 가져오기
        SiteUser siteUser = userService.getByUsername(username); // 사용자 정보 조회
        try{
            List<AutoInsurance> autoInsuranceList = insuranceService.getAutoBySiteUser(siteUser); // 자동차보험 가입 여부 확인
            return ResponseUtil.createSuccessResponse("status",false);
        }catch (DataNotFoundException e){
            return ResponseUtil.createSuccessResponse("status",true); // 미가입 상태 반환
        }
    }

    @PreAuthorize("isAuthenticated")
    @PostMapping("/auto")
    public ResponseEntity<?> postAutoProduct(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody AutoJoinDTO autoJoinDTO){
        // 비동기 처리 - 건강보험 가입 요청 처리
        /*   ai에게 가입 가능 판별 및 리스크 책정 후 메일로 결과 발송?  */
        String username = userDetails.getUsername(); // 인증된 사용자 이름 가져오기
        SiteUser siteUser = userService.getByUsername(username);
        insuranceService.createAuto(siteUser,autoJoinDTO);
        return ResponseUtil.createSuccessResponse("message","가입 신청 되었습니다.");
    }

    @PreAuthorize("isAuthenticated")
    @GetMapping("/fire")
    public ResponseEntity<?> getFireProduct(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername(); // 인증된 사용자 이름 가져오기
        SiteUser siteUser = userService.getByUsername(username); // 사용자 정보 조회
        try{
            List<FireInsurance> fireInsuranceList = insuranceService.getFireBySiteUser(siteUser); // 화재보험 가입 여부 확인
            return ResponseUtil.createSuccessResponse("status",false);
        }catch (DataNotFoundException e){
            return ResponseUtil.createSuccessResponse("status",true); // 미가입 상태 반환
        }
    }


    @PreAuthorize("isAuthenticated")
    @PostMapping("/fire")
    public ResponseEntity<?> postFireProduct(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody FireJoinDTO fireJoinDTO){
        // 비동기 처리 - 화재보험 가입 요청 처리
        /*   ai에게 가입 가능 판별 및 리스크 책정  */
        String username = userDetails.getUsername(); // 인증된 사용자 이름 가져오기
        return ResponseUtil.createSuccessResponse("message","가입 신청 되었습니다."); // 신청 완료 응답
    }
}
