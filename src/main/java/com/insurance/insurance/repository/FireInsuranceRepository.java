package com.insurance.insurance.repository;

import com.insurance.insurance.entity.FireInsurance;
import com.insurance.insurance.entity.Insurance;
import com.insurance.insurance.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FireInsuranceRepository extends JpaRepository<FireInsurance,Integer> {
    Optional<FireInsurance> findBySiteUserAndId(SiteUser siteUser,int id);

    Optional<List<FireInsurance>> findAllBySiteUser(SiteUser siteUser);

    Optional<FireInsurance> findBySiteUserAndPropertyAddressAndBuildingType(SiteUser siteUser, String propertyAddress, String buildingType);

    List<FireInsurance> findAllBySiteUserAndStatus(SiteUser siteUser, String status);
}
