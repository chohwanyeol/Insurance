package com.insurance.insurance.controller;



import com.insurance.insurance.entity.Product;
import com.insurance.insurance.exception.DataNotFoundException;
import com.insurance.insurance.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {
    private final ProductService productService;

    //메인화면
    @GetMapping("")
    public ResponseEntity<?> root(){
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .header("Location", "/main")
                .build();
    }

    //메인화면
    @GetMapping("")
    public ResponseEntity<?> main(){
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .header("Location", "/main")
                .build();
    }

}
