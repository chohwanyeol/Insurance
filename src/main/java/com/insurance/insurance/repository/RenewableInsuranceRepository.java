package com.insurance.insurance.repository;

import com.insurance.insurance.entity.RenewableInsurance;
import com.insurance.insurance.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RenewableInsuranceRepository extends JpaRepository<RequestRepository,Integer> {
    List<RenewableInsurance> findBySiteUser(SiteUser siteUser);

    RenewableInsurance findBySiteUserAndId(SiteUser siteUser, Integer id);
}
