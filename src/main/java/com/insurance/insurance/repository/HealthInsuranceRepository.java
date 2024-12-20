package com.insurance.insurance.repository;

import com.insurance.insurance.entity.HealthInsurance;
import com.insurance.insurance.entity.Insurance;
import com.insurance.insurance.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HealthInsuranceRepository extends JpaRepository<HealthInsurance,Integer> {
    Optional<HealthInsurance> findBySiteUser(SiteUser siteUser);
}
