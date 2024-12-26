package com.insurance.insurance.controller;


import com.insurance.insurance.dto.*;
import com.insurance.insurance.entity.*;
import com.insurance.insurance.exception.DataNotFoundException;
import com.insurance.insurance.service.*;
import com.insurance.insurance.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RequestMapping("/request")
@Controller
@RequiredArgsConstructor
public class RequestController {

    private final UserService userService;
    private final InsuranceService insuranceService;
    private final RequestService requestService;
    private final TransactionService transactionService;


    //보험금 청구 페이지
    @PreAuthorize("isAuthenticated")
    @GetMapping("")
    public ResponseEntity<?> getInsurances(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        SiteUser siteUser = userService.getByUsername(username);
        List<Insurance> insuranceList= insuranceService.getBySiteUserAndStatus(siteUser,"active");
        List<ProductDTO> productDTOList = insuranceList.stream()
                .map(insurance -> {
                    ProductDTO productDTO = new ProductDTO();
                    productDTO.EntityToDTO(insurance.getProduct());
                    return productDTO;
                })
                .toList();
        return ResponseEntity.ok(productDTOList);
    }



    //화재보험금 청구 페이지
    @PreAuthorize("isAuthenticated")
    @GetMapping("/fire")
    public ResponseEntity<?> getFireInsurances(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        SiteUser siteUser = userService.getByUsername(username);
        try {
            List<FireInsurance> fireInsuranceList = insuranceService.getFireBySiteUserAndStatus(siteUser,"active");
            List<FireInsuranceDTO> fireInsuranceDTOList = fireInsuranceList.stream()
                    .map(fireInsurance ->{
                        FireInsuranceDTO fireInsuranceDTO = new FireInsuranceDTO();
                        fireInsuranceDTO.EntityToDTO(fireInsurance);
                        return fireInsuranceDTO;
                    })
                    .toList();
            return ResponseEntity.ok(fireInsuranceDTOList);
        }catch (DataNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",false));
        }
    }

    //화재보험금 청구 페이지
    @PreAuthorize("isAuthenticated")
    @GetMapping("/fire/{fireId}")
    public ResponseEntity<?> getFireInsurances(@PathVariable("fireId")int id, @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        SiteUser siteUser = userService.getByUsername(username);
        try {
            FireInsurance fireInsurance = insuranceService.getFireBySiteUserAndId(siteUser,id);
            FireInsuranceDTO fireInsuranceDTO = new FireInsuranceDTO();
            fireInsuranceDTO.EntityToDTO(fireInsurance);
            return ResponseEntity.ok(fireInsuranceDTO);
        }catch (DataNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",false));
        }
    }


    //화재보험금 청구
    @PreAuthorize("isAuthenticated")
    @PostMapping("/fire/{fireId}")
    public ResponseEntity<?> postFireInsurances(@PathVariable("fireId")int id,@AuthenticationPrincipal UserDetails userDetails, @Valid FireRequestDTO fireRequestDTO){
        String username = userDetails.getUsername();
        SiteUser siteUser = userService.getByUsername(username);
        try {
            FireInsurance fireInsurance = insuranceService.getFireBySiteUserAndId(siteUser,id);

            //비동기 신청
            CompletableFuture<Request> requestFuture = requestService.requestFire(siteUser, fireRequestDTO, id);
            requestFuture.thenCompose(request -> transactionService.transaction(siteUser, request));

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
            List<AutoInsurance> autoInsuranceList = insuranceService.getAutoBySiteUserAndStatus(siteUser,"active");
            List<AutoInsuranceDTO> autoInsuranceDTOList = autoInsuranceList.stream()
                    .map(autoInsurance -> {
                AutoInsuranceDTO autoInsuranceDTO = new AutoInsuranceDTO();
                autoInsuranceDTO.EntityToDTO(autoInsurance);
                return autoInsuranceDTO;
            }).toList();
            return ResponseEntity.ok(autoInsuranceDTOList);
        }catch (DataNotFoundException e){
            return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST,"보험이 가입되어있지 않습니다.");
        }
    }

    //자동차보험금 청구 페이지
    @PreAuthorize("isAuthenticated")
    @GetMapping("/auto/{autoId}")
    public ResponseEntity<?> getAutoInsurance(@PathVariable("autoId")int id, @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        SiteUser siteUser = userService.getByUsername(username);
        try {
            AutoInsurance autoInsurance = insuranceService.getAutoBySiteUserAndId(siteUser, id);
            AutoInsuranceDTO autoInsuranceDTO = new AutoInsuranceDTO();
            autoInsuranceDTO.EntityToDTO(autoInsurance);
            return ResponseEntity.ok(autoInsuranceDTO);
        }catch (DataNotFoundException e){
            return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST,"보험이 가입되어있지 않습니다.");
        }
    }


    //자동차보험금 청구 페이지
    @PreAuthorize("isAuthenticated")
    @PostMapping("/auto/{autoId}")
    public ResponseEntity<?> postAutoInsurance(@PathVariable("autoId")int id, @AuthenticationPrincipal UserDetails userDetails, @Valid AutoRequestDTO autoRequestDTO){
        String username = userDetails.getUsername();
        SiteUser siteUser = userService.getByUsername(username);
        try {
            AutoInsurance autoInsurance = insuranceService.getAutoBySiteUserAndId(siteUser, id);
            //비동기 신청
            CompletableFuture<Request> requestFuture = requestService.requestAuto(siteUser, autoRequestDTO, id);
            requestFuture.thenCompose(request -> transactionService.transaction(siteUser, request));
            //결과 메일


            return ResponseEntity.ok(Map.of("status",true));
        }catch (DataNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",false));
        }
    }


    //건강보험금 청구 페이지
    @PreAuthorize("isAuthenticated")
    @GetMapping("/health")
    public ResponseEntity<?> getHealthInsurances(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        SiteUser siteUser = userService.getByUsername(username);
        try {
            HealthInsurance healthInsurance = insuranceService.getHealthBySiteUserAndStatus(siteUser,"active");
            HealthInsuranceDTO healthInsuranceDTO = new HealthInsuranceDTO();
            healthInsuranceDTO.EntityToDTO(healthInsurance);
            return ResponseEntity.ok(healthInsuranceDTO);
        }catch (DataNotFoundException e){
            return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST,"false");
        }
    }


    //건강보험금 청구 페이지
    @PreAuthorize("isAuthenticated")
    @PostMapping("/health/{healthId}")
    public ResponseEntity<?> postHealthInsurances(@PathVariable("healthId")int id,@AuthenticationPrincipal UserDetails userDetails, @Valid HealthRequestDTO healthRequestDTO){
        String username = userDetails.getUsername();
        SiteUser siteUser = userService.getByUsername(username);
        try {
            HealthInsurance healthInsurance = insuranceService.getHealthBySiteUserAndId(siteUser,id);
            if (ChronoUnit.DAYS.between(healthRequestDTO.getDate(), healthInsurance.getStartDate()) < 0) {
                return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST,"보험계약 날짜 내에 발생한 비용만 청구 가능합니다.");
            }
            //비동기 신청
            CompletableFuture<Request> requestFuture = requestService.requestHealth(siteUser, healthRequestDTO, id);
            requestFuture.thenCompose(request -> transactionService.transaction(siteUser, request));
            return ResponseUtil.createSuccessResponse("status",true);
        }catch (DataNotFoundException e){
            return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST,"보험이 가입되어있지 않습니다.");
        }
    }








}
