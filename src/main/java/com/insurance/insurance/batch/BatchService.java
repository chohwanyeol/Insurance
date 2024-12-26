package com.insurance.insurance.batch;


import com.insurance.insurance.entity.Insurance;
import com.insurance.insurance.entity.RenewableInsurance;
import com.insurance.insurance.service.InsuranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchService {
    private final InsuranceService insuranceService;

    private void deletePending(){
        insuranceService.deleteAllByStatus("pending");
    }

    private List<Insurance> getRenewable(){
        List<Insurance> insuranceList = insuranceService.getInsuranceRenewable();
        return insuranceList;
    }

    private void setRenewable(List<Insurance> insuranceList){
        insuranceService.setInsuranceRenewable(insuranceList);
    }

    private void setExpired(){
        insuranceService.setInsuranceExpired();
    }

    private List<RenewableInsurance> getDontRenew(){
        return insuranceService.getDontRenew();
    }

    private void setDontRenew(List<RenewableInsurance> renewableInsuranceList){
        insuranceService.setDontRenew(renewableInsuranceList);
    }

}
