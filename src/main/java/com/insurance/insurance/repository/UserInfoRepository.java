package com.insurance.insurance.repository;

import com.insurance.insurance.entity.SiteUser;
import com.insurance.insurance.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findBySiteUser(SiteUser siteUser);

}
