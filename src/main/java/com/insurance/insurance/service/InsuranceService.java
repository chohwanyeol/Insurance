package com.insurance.insurance.service;

import com.insurance.insurance.entity.*;
import com.insurance.insurance.exception.DataNotFoundException;
import com.insurance.insurance.repository.AutoInsuranceRepository;
import com.insurance.insurance.repository.FireInsuranceRepository;
import com.insurance.insurance.repository.HealthInsuranceRepository;
import com.insurance.insurance.repository.InsuranceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InsuranceService {
    private final InsuranceRepository insuranceRepository;
    private final FireInsuranceRepository fireInsuranceRepository;
    private final AutoInsuranceRepository autoInsuranceRepository;
    private final HealthInsuranceRepository healthInsuranceRepository;


    public List<Insurance> getBySiteUser(SiteUser siteUser) {
        return insuranceRepository.findAllBySiteUser(siteUser);
    }

    public FireInsurance getFireBySiteUser(SiteUser siteUser) {
        FireInsurance fireInsurance = fireInsuranceRepository.findBySiteUser(siteUser)
                .orElseThrow(()->new DataNotFoundException("해당 가입내용이 존재하지 않습니다."));
        return fireInsurance;
    }

    public AutoInsurance getAutoBySiteUser(SiteUser siteUser) {
        AutoInsurance autoInsurance = autoInsuranceRepository.findBySiteUser(siteUser)
                .orElseThrow(()->new DataNotFoundException("해당 가입내용이 존재하지 않습니다."));
        return autoInsurance;
    }

    public HealthInsurance getHealthBySiteUser(SiteUser siteUser) {
        HealthInsurance healthInsurance = healthInsuranceRepository.findBySiteUser(siteUser)
                .orElseThrow(()->new DataNotFoundException("해당 가입내용이 존재하지 않습니다."));
        return healthInsurance;
    }

}
