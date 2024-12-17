package com.insurance.insurance.repository;

import com.insurance.insurance.entity.Request;
import com.insurance.insurance.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findAllBySiteUser(SiteUser siteUser);

    Request findBySiteUserAndId(SiteUser siteUser, Integer id);
}
