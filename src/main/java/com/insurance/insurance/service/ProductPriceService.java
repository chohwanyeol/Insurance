package com.insurance.insurance.service;

import com.insurance.insurance.entity.Product;
import com.insurance.insurance.entity.ProductPrice;
import com.insurance.insurance.repository.ProductPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductPriceService {
    private final ProductPriceRepository productPriceRepository;
    public ProductPrice getByProduct(Product product) {
        return productPriceRepository.findByProduct(product);
    }
}
