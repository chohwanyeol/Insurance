package com.insurance.insurance.service;


import com.insurance.insurance.dto.HealthRequestDTO;
import com.insurance.insurance.entity.*;
import com.insurance.insurance.exception.DataNotFoundException;
import com.insurance.insurance.repository.RequestRepository;
import com.insurance.insurance.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserInfoRepository userInfoRepository;
    private final OCRService ocrService;
    private final ChatGPTService chatGPTService;
    private final InsuranceService insuranceService;
    private final TransactionService transactionService;


    public CompletableFuture<Request> requestHealth(SiteUser siteUser, HealthRequestDTO healthRequestDTO){
        UserInfo userInfo = userInfoRepository.findBySiteUser(siteUser).orElseThrow(
                ()->new DataNotFoundException("해당 유저가 없습니다."));


        //dto정보들 받아오기
        String content = healthRequestDTO.getContent();
        int price = healthRequestDTO.getPrice();
        try {
            String receipt = ocrService.extractText(healthRequestDTO.getReceiptImage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String claimType = healthRequestDTO.getClaimType();
        String hospitalName = healthRequestDTO.getHospitalName();
        List<String> extractedTexts = healthRequestDTO.getAdditionalDocuments().stream()
                .map(file -> {
                    try {
                        return ocrService.extractText(file); // OCR 수행
                    } catch (IOException e) {
                        throw new RuntimeException("파일 처리 실패: " + e.getMessage(), e);
                    }
                })
                .toList(); // Stream 결과를 List로 변환

        String userMessage ="여기넣어";
        String responseMessage;
        try {
            responseMessage = chatGPTService.getChatGPTResponse(userMessage);
        } catch (Exception e) {
            //throw
        }

        //메시지검증


        HealthInsurance healthInsurance = insuranceService.getHealthBySiteUser(siteUser);
        Insurance insurance = healthInsurance.getInsurance();
        RiskRank riskRank = insurance.getRiskRank();
        Integer deductible_rate = riskRank.getDeductible_rate();
        Integer coverage_limit = riskRank.getCoverage_limit();
        price = (int)Math.min(price * (deductible_rate/100), coverage_limit);
        //request 생성
        LocalDateTime request_date = LocalDateTime.now();
        Request request = new Request(insurance,claimType,content,price,request_date,"확인중");

        request= requestRepository.save(request);
        return CompletableFuture.completedFuture(request);

    }


    public List<Request> getBySiteUser(SiteUser siteUser) {
        return requestRepository.findAllBySiteUser(siteUser);
    }

    public Request getBySiteUserAndId(SiteUser siteUser, Integer id) {
        return requestRepository.findBySiteUserAndId(siteUser,id);
    }
}
