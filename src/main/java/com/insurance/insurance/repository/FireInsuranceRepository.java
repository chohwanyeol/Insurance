package com.insurance.insurance.repository;

import com.insurance.insurance.entity.FireInsurance;
import com.insurance.insurance.entity.Insurance;
import com.insurance.insurance.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FireInsuranceRepository extends JpaRepository<FireInsurance,Integer> {
    Optional<FireInsurance> findBySiteUser(SiteUser siteUser);
}