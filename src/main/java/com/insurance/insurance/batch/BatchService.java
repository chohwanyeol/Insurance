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

    public void deletePending(){
        insuranceService.deleteAllByStatus("pending");
    }

    public List<Insurance> getRenewable(){
        List<Insurance> insuranceList = insuranceService.getInsuranceRenewable();
        return insuranceList;
    }

    public void setRenewable(List<Insurance> insuranceList){
        insuranceService.setInsuranceRenewable(insuranceList);
    }

    public void setExpired(){
        insuranceService.setInsuranceExpired();
    }

    public List<RenewableInsurance> getDontRenew(){
        return insuranceService.getDontRenew();
    }

    public void setDontRenew(List<RenewableInsurance> renewableInsuranceList){
        insuranceService.setDontRenew(renewableInsuranceList);
    }

}
