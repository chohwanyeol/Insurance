package com.insurance.insurance.repository;

import com.insurance.insurance.entity.AutoInsurance;
import com.insurance.insurance.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AutoInsuranceRepository extends JpaRepository<AutoInsurance,Integer> {
    Optional<AutoInsurance> findBySiteUser(SiteUser siteUser);
}
