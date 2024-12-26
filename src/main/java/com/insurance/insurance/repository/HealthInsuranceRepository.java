package com.insurance.insurance.repository;

import com.insurance.insurance.entity.HealthInsurance;
import com.insurance.insurance.entity.Insurance;
import com.insurance.insurance.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HealthInsuranceRepository extends JpaRepository<HealthInsurance,Integer> {
    Optional<List<HealthInsurance>> findAllBySiteUser(SiteUser siteUser);

    Optional<HealthInsurance> findBySiteUserAndId(SiteUser siteUser, int id);

    HealthInsurance findBySiteUserAndStatus(SiteUser siteUser, String status);
}
