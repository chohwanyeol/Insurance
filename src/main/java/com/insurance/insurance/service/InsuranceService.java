package com.insurance.insurance.service;

import com.insurance.insurance.entity.Insurance;
import com.insurance.insurance.entity.SiteUser;
import com.insurance.insurance.repository.InsuranceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InsuranceService {
    private final InsuranceRepository insuranceRepository;
    public List<Insurance> getBySiteUser(SiteUser siteUser) {
        return insuranceRepository.findAllBySiteUser(siteUser);

    }
}
