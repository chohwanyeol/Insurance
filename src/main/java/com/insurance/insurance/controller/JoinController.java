package com.insurance.insurance.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/join")
public class JoinController {

    @PreAuthorize("isAuthenticated")
    @GetMapping("/product/{id}")
    public String getJoinProduct(){
        return "join";
    }


    @PreAuthorize("isAuthenticated")
    @PostMapping("/product/{id}")
    public String postJoinProduct(){
        return "join";
    }

}
