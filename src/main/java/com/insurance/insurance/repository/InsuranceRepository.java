package com.insurance.insurance.repository;

import com.insurance.insurance.entity.Insurance;
import com.insurance.insurance.entity.Product;
import com.insurance.insurance.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InsuranceRepository extends JpaRepository<Insurance, Integer> {
    List<Insurance> findAllBySiteUser(SiteUser siteUser);

    List<Insurance> findBySiteUserAndProduct(SiteUser siteUser, Product product);

    Optional<Insurance> findBySiteUserAndId(SiteUser siteUser, Integer id);
}
