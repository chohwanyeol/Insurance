package com.insurance.insurance.service;

import com.insurance.insurance.entity.Product;
import com.insurance.insurance.entity.RiskRank;
import com.insurance.insurance.repository.RiskRankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiskRankService {
    private final RiskRankRepository riskRankRepository;


    public RiskRank getByNameAndProduct(String rank, Product product) {
        return riskRankRepository.findByNameAndProduct(rank,product);
    }
}
