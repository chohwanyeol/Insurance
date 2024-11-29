package com.insurance.insurance.controller;


import com.insurance.insurance.entity.Product;
import com.insurance.insurance.exception.DataNotFoundException;
import com.insurance.insurance.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/product/{id}")
    public ResponseEntity<?> SomeProduct (@PathVariable("id") Integer id){
        try {
            Product product = productService.getById(id);
            return ResponseEntity.ok(product);
        }catch (DataNotFoundException e){
            return ResponseEntity.badRequest().body("해당 상품이 존재하지 않습니다.");
        }
    }
}
