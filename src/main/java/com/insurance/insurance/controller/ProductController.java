package com.insurance.insurance.controller;


import com.insurance.insurance.entity.Product;
import com.insurance.insurance.exception.DataNotFoundException;
import com.insurance.insurance.service.ProductService;
import com.insurance.insurance.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    //모든상품확인
    @GetMapping("/product")
    public ResponseEntity<?> AllProduct (){
        return ResponseEntity.ok(productService.getAll());
    }

    //특정상품확인
    @GetMapping("/product/{name}")
    public ResponseEntity<?> SomeProduct (@PathVariable("name") String name){
        try {
            Product product = productService.getByName(name);
            return ResponseEntity.ok(product);
        }catch (DataNotFoundException e){
            return ResponseUtil.createErrorResponse(
                    HttpStatus.BAD_REQUEST, "해당 상품이 존재하지 않습니다."
            );
        }
    }
}

