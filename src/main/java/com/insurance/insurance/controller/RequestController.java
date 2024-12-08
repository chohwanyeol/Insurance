package com.insurance.insurance.controller;


import com.insurance.insurance.dto.AutoRequestDTO;
import com.insurance.insurance.dto.FireRequestDTO;
import com.insurance.insurance.dto.HealthRequestDTO;
import com.insurance.insurance.entity.*;
import com.insurance.insurance.exception.DataNotFoundException;
import com.insurance.insurance.repository.AutoInsuranceRepository;
import com.insurance.insurance.repository.FireInsuranceRepository;
import com.insurance.insurance.repository.HealthInsuranceRepository;
import com.insurance.insurance.service.InsuranceService;
import com.insurance.insurance.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping("/request")
@Controller
@RequiredArgsConstructor
public class RequestController {

    private final UserService userService;
    private final InsuranceService insuranceService;



    //보험금 청구 페이지
    @PreAuthorize("isAuthenticated")
    @GetMapping("")
    public ResponseEntity<?> getInsurances(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        SiteUser siteUser = userService.getByUsername(username);
        List<Insurance> insuranceList= insuranceService.getBySiteUser(siteUser);
        List<Product> productList = insuranceList.stream().map(Insurance::getProduct).toList();
        return ResponseEntity.ok(productList);
    }



    //화재보험금 청구 페이지
    @PreAuthorize("isAuthenticated")
    @GetMapping("/fire")
    public ResponseEntity<?> getFireInsurances(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        SiteUser siteUser = userService.getByUsername(username);
        try {
            FireInsurance fireInsurance = insuranceService.getFireBySiteUser(siteUser);
            return ResponseEntity.ok(Map.of("status",true));
        }catch (DataNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",false));
        }
    }


    //화재보험금 청구
    @PreAuthorize("isAuthenticated")
    @PostMapping("/fire")
    public ResponseEntity<?> postFireInsurances(@AuthenticationPrincipal UserDetails userDetails, @Valid FireRequestDTO fireRequestDTO){
        String username = userDetails.getUsername();
        SiteUser siteUser = userService.getByUsername(username);
        try {
            FireInsurance fireInsurance = insuranceService.getFireBySiteUser(siteUser);

            //비동기 신청
            //결과 메일

            return ResponseEntity.ok(Map.of("status",true));
        }catch (DataNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",false));
        }
    }



    //자동차보험금 청구 페이지
    @PreAuthorize("isAuthenticated")
    @GetMapping("/auto")
    public ResponseEntity<?> getAutoInsurances(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        SiteUser siteUser = userService.getByUsername(username);
        try {
            AutoInsurance autoInsurance = insuranceService.getAutoBySiteUser(siteUser);
            return ResponseEntity.ok(Map.of("status",true));
        }catch (DataNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",false));
        }
    }

    //자동차보험금 청구 페이지
    @PreAuthorize("isAuthenticated")
    @PostMapping("/auto")
    public ResponseEntity<?> postAutoInsurances(@AuthenticationPrincipal UserDetails userDetails, @Valid AutoRequestDTO autoRequestDTO){
        String username = userDetails.getUsername();
        SiteUser siteUser = userService.getByUsername(username);
        try {
            AutoInsurance autoInsurance = insuranceService.getAutoBySiteUser(siteUser);

            //비동기 신청
            //결과 메일


            return ResponseEntity.ok(Map.of("status",true));
        }catch (DataNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",false));
        }
    }



    //건강보험금 청구 페이지
    @PreAuthorize("isAuthenticated")
    @GetMapping("")
    public ResponseEntity<?> getHealthInsurances(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        SiteUser siteUser = userService.getByUsername(username);
        try {
            HealthInsurance healthInsurance = insuranceService.getHealthBySiteUser(siteUser);
            return ResponseEntity.ok(Map.of("status",true));
        }catch (DataNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",false));
        }
    }


    //건강보험금 청구 페이지
    @PreAuthorize("isAuthenticated")
    @PostMapping("")
    public ResponseEntity<?> postHealthInsurances(@AuthenticationPrincipal UserDetails userDetails, @Valid HealthRequestDTO healthRequestDTO){
        String username = userDetails.getUsername();
        SiteUser siteUser = userService.getByUsername(username);
        try {
            HealthInsurance healthInsurance = insuranceService.getHealthBySiteUser(siteUser);

            //비동기 신청
            //결과 메일


            return ResponseEntity.ok(Map.of("status",true));
        }catch (DataNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",false));
        }
    }








}
