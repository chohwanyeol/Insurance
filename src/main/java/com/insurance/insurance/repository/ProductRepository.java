package com.insurance.insurance.repository;

import com.insurance.insurance.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findAll();
    Optional<Product> findById(Integer id);

    Product findByName(String name);

    Optional<Product> findById(String name);
}
