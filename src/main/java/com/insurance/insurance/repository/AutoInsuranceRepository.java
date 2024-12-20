package com.insurance.insurance.repository;

import com.insurance.insurance.entity.AutoInsurance;
import com.insurance.insurance.entity.Insurance;
import com.insurance.insurance.entity.Product;
import com.insurance.insurance.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AutoInsuranceRepository extends JpaRepository<AutoInsurance,Integer> {
    
    Optional<List<AutoInsurance>> findAllBySiteUser(SiteUser siteUser);

    Optional<AutoInsurance> findBySiteUserAndId(SiteUser siteUser, Integer id);
}
