package com.insurance.insurance.repository;

import com.insurance.insurance.entity.Insurance;
import com.insurance.insurance.entity.RenewableInsurance;
import com.insurance.insurance.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RenewableInsuranceRepository extends JpaRepository<RenewableInsurance,Integer> {
    List<RenewableInsurance> findBySiteUser(SiteUser siteUser);

    Optional<RenewableInsurance> findBySiteUserAndId(SiteUser siteUser, Integer id);

    void deleteBySiteUserAndInsurance(SiteUser siteUser, Insurance insurance);


    @Query("""
    SELECT i FROM RenewableInsurance i
    WHERE FUNCTION('MONTH', i.createDate) = :targetMonth
      AND FUNCTION('DAY', i.createDate) = :targetDay
      AND FUNCTION('YEAR', i.createDate) != :targetYear
    """)
    List<RenewableInsurance> findAllDontRenew(@Param("targetYear") int targetYear,
                                              @Param("targetMonth") int targetMonth,
                                              @Param("targetDay") int targetDay );
}
