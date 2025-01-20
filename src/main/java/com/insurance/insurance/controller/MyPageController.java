package com.insurance.insurance.controller;


import com.insurance.insurance.dto.*;
import com.insurance.insurance.entity.*;
import com.insurance.insurance.exception.DataNotFoundException;
import com.insurance.insurance.service.InsuranceService;
import com.insurance.insurance.service.RequestService;
import com.insurance.insurance.service.TransactionService;
import com.insurance.insurance.service.UserService;
import com.insurance.insurance.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {


    private final UserService userService;
    private final InsuranceService insuranceService;
    private final RequestService requestService;
    private final TransactionService transactionService;


    //가입된보험
    @PreAuthorize("isAuthenticated")
    @GetMapping("/joined")
    public ResponseEntity<?> myInsurance(@AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        List<Insurance> insuranceList = insuranceService.getBySiteUserAndStatus(siteUser,"active");
        List<InsuranceDTO> insuranceDTOList = insuranceList.stream()
                .map(insurance -> {
                            InsuranceDTO insuranceDTO = new InsuranceDTO();
                            insuranceDTO.EntityToDTO(insurance);
                            return insuranceDTO;
                }).toList();
        return ResponseEntity.ok(insuranceDTOList);
    }

    //가입된보험
    @PreAuthorize("isAuthenticated")
    @GetMapping("/joined/{id}")
    public ResponseEntity<?> myInsuranceDetail(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        Insurance insurance = insuranceService.getBySiteUserAndId(siteUser,id);
        switch (insurance.getProduct().getName()){
            case "fire":
                FireInsuranceDTO fireInsuranceDTO = new FireInsuranceDTO();
                fireInsuranceDTO.EntityToDTO((FireInsurance) insurance);
                return ResponseEntity.ok(fireInsuranceDTO);
            case "auto":
                AutoInsuranceDTO autoInsuranceDTO = new AutoInsuranceDTO();
                autoInsuranceDTO.EntityToDTO((AutoInsurance)insurance);
                return ResponseEntity.ok(autoInsuranceDTO);
            default :
                HealthInsuranceDTO healthInsuranceDTO = new HealthInsuranceDTO();
                healthInsuranceDTO.EntityToDTO((HealthInsurance)insurance);
                return ResponseEntity.ok(healthInsuranceDTO);
        }
    }

    //보험해지
    @PreAuthorize("isAuthenticated")
    @PatchMapping("/joined/{id}/cancel")
    public ResponseEntity<?> myInsuranceCancel(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        Insurance insurance = insuranceService.getBySiteUserAndId(siteUser,id);
        insurance = insuranceService.cancel(insurance);
        InsuranceDTO insuranceDTO = new InsuranceDTO();
        insuranceDTO.EntityToDTO(insurance);
        return ResponseEntity.ok(insuranceDTO);
    }

    //보험정보수정
    @PreAuthorize("isAuthenticated")
    @PatchMapping("/joined/{id}/update")
    public ResponseEntity<?> myInsuranceUpdate(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid UpdateBankDTO updateBankDTO){
        try {
            SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
            Insurance insurance = insuranceService.getBySiteUserAndId(siteUser, id);
            insurance = insuranceService.updateBank(insurance, updateBankDTO);
            InsuranceDTO insuranceDTO = new InsuranceDTO();
            insuranceDTO.EntityToDTO(insurance);
            return ResponseEntity.ok(insuranceDTO);
        }catch (DataNotFoundException e){
            return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST,"해당 보험이 존재하지 않습니다.");
        }
    }


    @PreAuthorize("isAuthenticated")
    @GetMapping("/requested")
    public ResponseEntity<?> myRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("requestDate"));
        Pageable pageable = PageRequest.of(page, size,Sort.by(sorts));
        Page<Request> requestPage = requestService.getBySiteUser(siteUser, pageable);

        // DTO 변환
        List<RequestDTO> requestDTOList = requestPage.getContent().stream()
                .map(request -> {
                    RequestDTO requestDTO = new RequestDTO();
                    requestDTO.EntityToDTO(request);
                    return requestDTO;
                }).toList();

        // 응답에 페이징 메타데이터 포함
        Map<String, Object> response = new HashMap<>();
        response.put("content", requestDTOList);
        response.put("currentPage", requestPage.getNumber());
        response.put("totalItems", requestPage.getTotalElements());
        response.put("totalPages", requestPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    //청구내용
    @PreAuthorize("isAuthenticated")
    @GetMapping("/requested/{id}")
    public ResponseEntity<?> myRequestDetail(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        Request request = requestService.getBySiteUserAndId(siteUser,id);
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.EntityToDTO(request);
        return ResponseEntity.ok(requestDTO);
    }

    //지급내용
    @PreAuthorize("isAuthenticated")
    @GetMapping("/transaction")
    public ResponseEntity<?> myTransaction(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("dateTime"));
        Pageable pageable = PageRequest.of(page,size,Sort.by(sorts));
        Page<Transaction> transactionPage = transactionService.getBySiteUser(siteUser,pageable);
        List<TransactionDTO> transactionDTOList =transactionPage.getContent().stream()
                .map(transaction -> {
                    TransactionDTO transactionDTO = new TransactionDTO();
                    transactionDTO.EntityToDTO(transaction);
                    return transactionDTO;
                }).toList();

        // 응답에 페이징 메타데이터 포함
        Map<String, Object> response = new HashMap<>();
        response.put("content", transactionDTOList);
        response.put("currentPage", transactionPage.getNumber());
        response.put("totalItems", transactionPage.getTotalElements());
        response.put("totalPages", transactionPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    //지급내용
    @PreAuthorize("isAuthenticated")
    @GetMapping("/transaction/{id}")
    public ResponseEntity<?> myTransactionDetail(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        Transaction transaction = transactionService.getBySiteUserAndId(siteUser,id);
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.EntityToDTO(transaction);
        return ResponseEntity.ok(transactionDTO);
    }


    //갱신
    @PreAuthorize("isAuthenticated")
    @GetMapping("/renewable")
    public ResponseEntity<?> renew(@AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        List<RenewableInsurance> renewableInsuranceList = insuranceService.getRenewableBySiteUser(siteUser);
        List<InsuranceDTO> insuranceDTOList = renewableInsuranceList.stream().
                map(renewableInsurance -> {
                    InsuranceDTO insuranceDTO = new InsuranceDTO();
                    insuranceDTO.EntityToDTO(renewableInsurance.getInsurance());
                    return insuranceDTO;
                }).toList();
        return ResponseEntity.ok(insuranceDTOList);
    }


    //갱신
    @PreAuthorize("isAuthenticated")
    @GetMapping("/renewable/{name}/{id}")
    public ResponseEntity<?> renewDetail(@PathVariable("name") String name, @PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        try{
            RenewableInsurance renewableInsurance = insuranceService.getRenewableBySiteUserAndId(siteUser,id);
            switch (name){
                case "fire":
                    FireInsuranceDTO fireInsuranceDTO = new FireInsuranceDTO();
                    fireInsuranceDTO.EntityToDTO(renewableInsurance.getInsurance());
                    return ResponseEntity.ok(fireInsuranceDTO);
                case "auto":
                    AutoInsuranceDTO autoInsuranceDTO = new AutoInsuranceDTO();
                    autoInsuranceDTO.EntityToDTO(renewableInsurance.getInsurance());
                    return ResponseEntity.ok(autoInsuranceDTO);
                default :
                    HealthInsuranceDTO healthInsuranceDTO = new HealthInsuranceDTO();
                    healthInsuranceDTO.EntityToDTO(renewableInsurance.getInsurance());
                    return ResponseEntity.ok(healthInsuranceDTO);
            }
        }catch (DataNotFoundException e){
            return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST,"해당 보험이 존재하지 않습니다.");
        }

    }

    //갱신
    @PreAuthorize("isAuthenticated")
    @PatchMapping("/renewable/fire/{id}")
    public ResponseEntity<?> renewFire(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails,
                                       @Valid FireRenewDTO fireRenewDTO){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        try{
            RenewableInsurance renewableInsurance = insuranceService.getRenewableBySiteUserAndId(siteUser,id);
            FireInsurance fireInsurance = insuranceService.updateFire(siteUser,id,fireRenewDTO);
            FireInsuranceDTO fireInsuranceDTO = new FireInsuranceDTO();
            fireInsuranceDTO.EntityToDTO(fireInsurance);
            insuranceService.deleteRenewable(renewableInsurance);
            return ResponseEntity.ok(fireInsuranceDTO);
        }catch (DataNotFoundException e){
            return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST,"데이터가 존재하지 않습니다.");
        }
    }

    //갱신
    @PreAuthorize("isAuthenticated")
    @PatchMapping("/renewable/auto/{id}")
    public ResponseEntity<?> renewAuto(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails,
                                       @Valid AutoRenewDTO autoRenewDTO){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        try{
            RenewableInsurance renewableInsurance = insuranceService.getRenewableBySiteUserAndId(siteUser,id);
            AutoInsurance autoInsurance = insuranceService.updateAuto(siteUser,id,autoRenewDTO);
            AutoInsuranceDTO autoInsuranceDTO = new AutoInsuranceDTO();
            autoInsuranceDTO.EntityToDTO(autoInsurance);
            insuranceService.deleteRenewable(renewableInsurance);
            return ResponseEntity.ok(autoInsuranceDTO);
        }catch (DataNotFoundException e){
            return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST,"데이터가 존재하지 않습니다.");
        }
    }

    //갱신
    @PreAuthorize("isAuthenticated")
    @PatchMapping("/renewable//health/{id}")
    public ResponseEntity<?> renewFire(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails,
                                       @Valid HealthRenewDTO healthRenewDTO){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        try{
            RenewableInsurance renewableInsurance = insuranceService.getRenewableBySiteUserAndId(siteUser,id);
            HealthInsurance healthInsurance = insuranceService.updateHealth(siteUser,id,healthRenewDTO);
            HealthInsuranceDTO healthInsuranceDTO = new HealthInsuranceDTO();
            healthInsuranceDTO.EntityToDTO(healthInsurance);
            insuranceService.deleteRenewable(renewableInsurance);
            return ResponseEntity.ok(healthInsuranceDTO);
        }catch (DataNotFoundException e){
            return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST,"데이터가 존재하지 않습니다.");
        }
    }
}
