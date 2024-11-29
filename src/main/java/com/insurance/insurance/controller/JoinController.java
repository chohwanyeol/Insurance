package com.insurance.insurance.controller;


import com.insurance.insurance.dto.AutoDTO;
import com.insurance.insurance.dto.FireDTO;
import com.insurance.insurance.dto.HealthDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/join")
public class JoinController {

    @PreAuthorize("isAuthenticated")
    @GetMapping("/product/health")
    public String getHealthProduct(){
        return "health";
    }

    @PreAuthorize("isAuthenticated")
    @GetMapping("/product/auto")
    public String getAutoProduct(){
        return "auto";
    }

    @PreAuthorize("isAuthenticated")
    @GetMapping("/product/fire")
    public String getFireProduct(){
        return "fire";
    }


    @PreAuthorize("isAuthenticated")
    @PostMapping("/product/health")
    public ResponseEntity<?> postHealthProduct(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody HealthDTO healthDTO){
        //비동기처리
        /*   ai에게 가입 가능 판별 및 리스크 책정  */
        String username = userDetails.getUsername();
        return ResponseEntity.ok("가입 신청 되었습니다.");
    }

    @PreAuthorize("isAuthenticated")
    @PostMapping("/product/auto")
    public ResponseEntity<?> postAutoProduct(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody AutoDTO autoDTO){
        //비동기처리
        /*   ai에게 가입 가능 판별 및 리스크 책정  */
        String username = userDetails.getUsername();
        return ResponseEntity.ok("가입 신청 되었습니다.");
    }

    @PreAuthorize("isAuthenticated")
    @PostMapping("/product/fire")
    public ResponseEntity<?> postFireProduct(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody FireDTO fireDTO){
        //비동기처리
        /*   ai에게 가입 가능 판별 및 리스크 책정  */
        String username = userDetails.getUsername();
        return ResponseEntity.ok("가입 신청 되었습니다.");
    }

}
