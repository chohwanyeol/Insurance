package com.insurance.insurance.repository;

import com.insurance.insurance.entity.Insurance;
import com.insurance.insurance.entity.RiskRank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiskRankRepository extends JpaRepository<RiskRank, Integer> {

    RiskRank findByName(String riskRank);
}