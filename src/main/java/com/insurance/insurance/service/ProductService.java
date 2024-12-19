package com.insurance.insurance.service;


import com.insurance.insurance.entity.Product;
import com.insurance.insurance.exception.DataNotFoundException;
import com.insurance.insurance.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    //테이블 정보 모두 가져오기
    public List<Product> getAll(){
        return productRepository.findAll();
    }

    public Product getById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Product with id " + id + " not found"));

    }

    public Product getByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(()-> new DataNotFoundException("Product with name " + name + " not found"));
    }
}
