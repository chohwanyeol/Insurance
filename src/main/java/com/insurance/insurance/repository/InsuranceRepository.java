package com.insurance.insurance.repository;

import com.insurance.insurance.entity.Insurance;
import com.insurance.insurance.entity.Product;
import com.insurance.insurance.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InsuranceRepository extends JpaRepository<Insurance, Integer> {
    List<Insurance> findAllBySiteUser(SiteUser siteUser);

    List<Insurance> findBySiteUserAndProduct(SiteUser siteUser, Product product);

    Optional<Insurance> findBySiteUserAndId(SiteUser siteUser, Integer id);

    void deleteAllByStatus(String status);

    @Query("""
    SELECT i FROM Insurance i
    WHERE FUNCTION('MONTH', i.endDate) = :targetMonth
      AND FUNCTION('DAY', i.endDate) = :targetDay
      AND FUNCTION('YEAR', i.endDate) != :targetYear
      AND i.status = 'active'
    """)
    List<Insurance> findAllRenewable(@Param("targetYear") int targetYear,
                                     @Param("targetMonth") int targetMonth,
                                     @Param("targetDay") int targetDay );


    List<Insurance> findAllByEndDate(LocalDate now);

    List<Insurance> findAllBySiteUserAndStatus(SiteUser siteUser, String status);
}
