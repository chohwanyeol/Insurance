package com.insurance.insurance.initializer;

import com.insurance.insurance.entity.Product;
import com.insurance.insurance.entity.RiskRank;
import com.insurance.insurance.repository.ProductRepository;
import com.insurance.insurance.repository.RiskRankRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductInitializer {

    private final ProductRepository productRepository;
    private final RiskRankRepository riskRankRepository;

    @PostConstruct
    @Transactional
    public void init() {
        if (productRepository.count() == 0) {
            //상품추가
            Product carInsurance = new Product("자동차보험",
                    "자동차 사고 발생 시 차량 수리비, 대인 및 대물 사고에 대한 종합적인 보장을 제공합니다. " +
                            "출퇴근, 장거리 운전, 영업용 차량 등 다양한 차량이 가입할 수 있습니다. " +
                            "보장 항목으로는 대인 보상, 대물 보상, 차량 손상 보상이 포함됩니다. " +
                            "단, 음주운전이나 무면허 운전 사고는 보장 대상에서 제외됩니다.",
                    100000,
                    LocalDate.now());

            Product healthInsurance = new Product("건강보험",
                    "질병 및 사고로 인한 입원비, 수술비, 약제비를 보장하는 종합 건강보험입니다. " +
                            "특히 암, 심혈관 질환, 뇌졸중 등 중증 질환에 대한 진단비와 치료비를 포함하여 보장합니다. " +
                            "주요 보장 항목은 입원비, 수술비, 약제비이며, 고객의 연령, 병력, 생활 습관을 고려한 보험료가 책정됩니다. " +
                            "정기적인 건강 관리를 위한 필수적인 보험입니다.",
                    120000,
                    LocalDate.now());

            Product fireInsurance = new Product("화재보험",
                    "예상치 못한 화재 사고로 인한 재산 피해를 보장합니다. " +
                            "주택, 상업시설, 가재도구 등 다양한 자산에 대한 손실을 보상하며, " +
                            "화재로 인해 발생한 대체 거주 비용과 구조 비용도 지원합니다. " +
                            "보장 항목으로는 주택 보상, 상업시설 보상, 가재도구 보상이 포함됩니다. " +
                            "안심할 수 있는 생활을 위해 필수적인 보험입니다.",
                    100000,
                    LocalDate.now());

            carInsurance = productRepository.save(carInsurance);
            healthInsurance = productRepository.save(healthInsurance);
            fireInsurance = productRepository.save(fireInsurance);



            RiskRank autoFull = new RiskRank("Full Coverage", carInsurance, 80, 120,
                    "대인, 대물, 자손 보장을 포함하며, 차량 수리 비용과 사고로 인한 대체 교통비를 모두 지원합니다.", 100.0);
            RiskRank autoMajor = new RiskRank("Major Coverage", carInsurance, 70, 90,
                    "대인, 대물, 자손 보장을 제공하며, 차량 수리는 지원되지 않습니다..", 130.0);
            RiskRank autoEssential = new RiskRank("Essential Coverage", carInsurance, 60, 65,
                    "대인, 대물보장만 제공되며, 자손 및 차량 수리 보장은 포함되지 않습니다.", 160.0);
            RiskRank autoEmergency = new RiskRank("Emergency Coverage", carInsurance, 50, 50,
                    "생명 구조 및 응급 대인 보장만 제공되며, 대물 보장 및 차량 관련 보장은 전혀 포함되지 않습니다.", 190.0);


            RiskRank healthFull = new RiskRank("Full Coverage", healthInsurance,80,120,
                    "비급여 항목을 제외하고, 입원, 수술, 약제비, 암, 심혈관 등 전체 보장",100.0);
            RiskRank healthMajor = new RiskRank("Major Coverage", healthInsurance,70,100,
                    "비급여 항목을 제외하고, 입원, 수술비 보장. 고가의 첨단 의료 기술에 대한 보장은 제한",110.0);
            RiskRank healthEssential = new RiskRank("Essential Coverage", healthInsurance,60,80,
                    "비급여 항목을 제외하고, 입원비만 보장. 고위험 질환(암, 심혈관) 제외",120.0);
            RiskRank healthEmergency = new RiskRank("Emergency Coverage", healthInsurance,50,50,
                    "비급여 항목을 제외하고, 응급 의료, 생명 유지 장비 보장. 정기치료 제외",130.0);


            RiskRank fireFull = new RiskRank("Full Coverage", fireInsurance, 80, 120,
                    "주택, 상업시설, 가재도구를 포함하며, 화재로 인한 추가 비용까지 모두 보장합니다.", 100.0);
            RiskRank fireMajor = new RiskRank("Major Coverage", fireInsurance, 70, 90,
                    "주택 및 상업시설만 보장하며, 가재도구 보장은 포함되지 않습니다.", 130.0);
            RiskRank fireEssential = new RiskRank("Essential Coverage", fireInsurance, 60, 65,
                    "주택 및 상업시설 보장을 제공하지만, 가재도구 보장은 포함되지 않습니다.", 160.0);
            RiskRank fireEmergency = new RiskRank("Emergency Coverage", fireInsurance, 50, 50,
                    "화재로 인한 생명 구조 비용만 보장하며, 기타 보장은 포함되지 않습니다.", 190.0);

            riskRankRepository.saveAll(List.of(
                    autoFull, autoMajor, autoEssential, autoEmergency,
                    healthFull, healthMajor, healthEssential, healthEmergency,
                    fireFull, fireMajor, fireEssential, fireEmergency
            ));

        }
    }

}
