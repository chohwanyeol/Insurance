package com.insurance.insurance.controller;


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
        return ResponseEntity.ok(insuranceList);
    }

    //가입된보험
    @GetMapping("/insurance/{id}")
    public ResponseEntity<?> myInsuranceDetail(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        Insurance insurance = insuranceService.getBySiteUserAndId(siteUser,id);
        return ResponseEntity.ok(insurance);
    }



    //청구내용
    @GetMapping("/request")
    public ResponseEntity<?> myRequest(@AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        List<Request> requestList = requestService.getBySiteUser(siteUser);
        return ResponseEntity.ok(requestList);
    }

    //청구내용
    @GetMapping("/request/{id}")
    public ResponseEntity<?> myRequestDetail(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        Request request = requestService.getBySiteUserAndId(siteUser,id);
        return ResponseEntity.ok(request);
    }

    //지급내용
    @GetMapping("/transaction")
    public ResponseEntity<?> myTransaction(@AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        List<Transaction> transactionList = transactionService.getBySiteUser(siteUser);
        return ResponseEntity.ok(transactionList);
    }

    //지급내용
    @GetMapping("/transaction/{id}")
    public ResponseEntity<?> myTransactionDetail(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        Transaction transaction = transactionService.getBySiteUserAndId(siteUser,id);
        return ResponseEntity.ok(transaction);
    }


    //갱신
    @GetMapping("/renewable")
    public ResponseEntity<?> renew(@AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        List<RenewableInsurance> renewableInsuranceList = insuranceService.getRenewableBySiteUser(siteUser);
        return ResponseEntity.ok(renewableInsuranceList);
    }


    //갱신
    @GetMapping("/renewable/{id}")
    public ResponseEntity<?> renewDetail(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails){
        SiteUser siteUser = userService.getByUsername(userDetails.getUsername());
        RenewableInsurance renewableInsurance = insuranceService.getRenewableBySiteUserAndId(siteUser,id);
        return ResponseEntity.ok(renewableInsurance);
    }

}
