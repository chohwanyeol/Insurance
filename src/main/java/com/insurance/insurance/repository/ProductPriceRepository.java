package com.insurance.insurance.repository;

import com.insurance.insurance.entity.Product;
import com.insurance.insurance.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Integer> {
    ProductPrice findByProduct(Product product);
}
