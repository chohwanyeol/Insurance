package com.insurance.insurance.repository;

import com.insurance.insurance.entity.SiteUser;
import com.insurance.insurance.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
    List<Transaction> findAllBySiteUser(SiteUser siteUser);

    Transaction findBySiteUserAndId(SiteUser siteUser, Integer id);

    Page<Transaction> findAllBySiteUser(SiteUser siteUser, Pageable pageable);
}
