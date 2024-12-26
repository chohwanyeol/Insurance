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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/insurance")
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
    @GetMapping("/insurance/{id}")
    public ResponseEntity<?> myInsuranceDetail(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        Insurance insurance = insuranceService.getBySiteUserAndId(siteUser,id);
        switch (insurance.getProduct().getName()){
            case "fire":
                FireInsuranceDTO fireInsuranceDTO = new FireInsuranceDTO();
                fireInsuranceDTO.EntityToDTO(insurance);
                return ResponseEntity.ok(fireInsuranceDTO);
            case "auto":
                AutoInsuranceDTO autoInsuranceDTO = new AutoInsuranceDTO();
                autoInsuranceDTO.EntityToDTO(insurance);
                return ResponseEntity.ok(autoInsuranceDTO);
            default :
                HealthInsuranceDTO healthInsuranceDTO = new HealthInsuranceDTO();
                healthInsuranceDTO.EntityToDTO(insurance);
                return ResponseEntity.ok(healthInsuranceDTO);
        }
    }

    //보험해지
    @GetMapping("/insurance/{id}/cancel")
    public ResponseEntity<?> myInsuranceCancel(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        Insurance insurance = insuranceService.getBySiteUserAndId(siteUser,id);
        insurance = insuranceService.cancel(insurance);
        InsuranceDTO insuranceDTO = new InsuranceDTO();
        insuranceDTO.EntityToDTO(insurance);
        return ResponseEntity.ok(insuranceDTO);
    }



    //청구내용
    @GetMapping("/request")
    public ResponseEntity<?> myRequest(@AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        List<Request> requestList = requestService.getBySiteUser(siteUser);
        List<RequestDTO> requestDTOList = requestList.stream()
                .map(request -> {
                    RequestDTO requestDTO = new RequestDTO();
                    requestDTO.EntityToDTO(request);
                    return requestDTO;
                }).toList();
        return ResponseEntity.ok(requestDTOList);
    }

    //청구내용
    @GetMapping("/request/{id}")
    public ResponseEntity<?> myRequestDetail(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        Request request = requestService.getBySiteUserAndId(siteUser,id);
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.EntityToDTO(request);
        return ResponseEntity.ok(requestDTO);
    }

    //지급내용
    @GetMapping("/transaction")
    public ResponseEntity<?> myTransaction(@AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        List<Transaction> transactionList = transactionService.getBySiteUser(siteUser);
        List<TransactionDTO> transactionDTOList =transactionList.stream()
                .map(transaction -> {
                    TransactionDTO transactionDTO = new TransactionDTO();
                    transactionDTO.EntityToDTO(transaction);
                    return transactionDTO;
                }).toList();
        return ResponseEntity.ok(transactionList);
    }

    //지급내용
    @GetMapping("/transaction/{id}")
    public ResponseEntity<?> myTransactionDetail(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        Transaction transaction = transactionService.getBySiteUserAndId(siteUser,id);
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.EntityToDTO(transaction);
        return ResponseEntity.ok(transactionDTO);
    }


    //갱신
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
    @GetMapping("/renewable/{id}")
    public ResponseEntity<?> renewDetail(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        RenewableInsurance renewableInsurance = insuranceService.getRenewableBySiteUserAndId(siteUser,id);
        switch (renewableInsurance.getInsurance().getProduct().getName()){
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
    }

    //갱신
    @GetMapping("/renewable/{id}/{name}")
    public ResponseEntity<?> renewInsurance(@PathVariable("id") Integer id,@PathVariable("name") String name, @AuthenticationPrincipal UserDetails userDetails){
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
            return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST,"데이터가 존재하지 않습니다.");
        }
    }

    //갱신
    @PostMapping("/renewable/{id}/fire")
    public ResponseEntity<?> renewFire(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails,
                                       @Valid FireRenewDTO fireRenewDTO){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        try{
            RenewableInsurance renewableInsurance = insuranceService.getRenewableBySiteUserAndId(siteUser,id);
            FireInsurance fireInsurance = insuranceService.updateFire(siteUser,id,fireRenewDTO);
            insuranceService.deleteRenewable(renewableInsurance);
            FireInsuranceDTO fireInsuranceDTO = new FireInsuranceDTO();
            fireInsuranceDTO.EntityToDTO(fireInsurance);
            return ResponseEntity.ok(fireInsuranceDTO);
        }catch (DataNotFoundException e){
            return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST,"데이터가 존재하지 않습니다.");
        }
    }

    //갱신
    @PostMapping("/renewable/{id}/auto")
    public ResponseEntity<?> renewAuto(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails,
                                       @Valid AutoRenewDTO autoRenewDTO){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        try{
            RenewableInsurance renewableInsurance = insuranceService.getRenewableBySiteUserAndId(siteUser,id);
            AutoInsurance autoInsurance = insuranceService.updateAuto(siteUser,id,autoRenewDTO);
            insuranceService.deleteRenewable(renewableInsurance);
            AutoInsuranceDTO autoInsuranceDTO = new AutoInsuranceDTO();
            autoInsuranceDTO.EntityToDTO(autoInsurance);
            return ResponseEntity.ok(autoInsuranceDTO);
        }catch (DataNotFoundException e){
            return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST,"데이터가 존재하지 않습니다.");
        }
    }

    //갱신
    @PostMapping("/renewable/{id}/health")
    public ResponseEntity<?> renewFire(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails,
                                       @Valid HealthRenewDTO healthRenewDTO){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        try{
            RenewableInsurance renewableInsurance = insuranceService.getRenewableBySiteUserAndId(siteUser,id);
            HealthInsurance healthInsurance = insuranceService.updateHealth(siteUser,id,healthRenewDTO);
            insuranceService.deleteRenewable(renewableInsurance);
            HealthInsuranceDTO healthInsuranceDTO = new HealthInsuranceDTO();
            healthInsuranceDTO.EntityToDTO(healthInsurance);
            return ResponseEntity.ok(healthInsuranceDTO);
        }catch (DataNotFoundException e){
            return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST,"데이터가 존재하지 않습니다.");
        }
    }



}
