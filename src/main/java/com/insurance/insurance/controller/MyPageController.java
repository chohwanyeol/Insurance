package com.insurance.insurance.controller;


import com.insurance.insurance.dto.InsuranceDTO;
import com.insurance.insurance.dto.RequestDTO;
import com.insurance.insurance.dto.TransactionDTO;
import com.insurance.insurance.entity.*;
import com.insurance.insurance.service.InsuranceService;
import com.insurance.insurance.service.RequestService;
import com.insurance.insurance.service.TransactionService;
import com.insurance.insurance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        List<Insurance> insuranceList = insuranceService.getBySiteUser(siteUser);
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
        InsuranceDTO insuranceDTO = new InsuranceDTO();
        insuranceDTO.EntityToDTO(insurance);
        return ResponseEntity.ok(insuranceDTO);
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
        InsuranceDTO insuranceDTO = new InsuranceDTO();
        insuranceDTO.EntityToDTO(renewableInsurance.getInsurance());
        return ResponseEntity.ok(insuranceDTO);
    }

}
