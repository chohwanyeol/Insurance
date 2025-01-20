package com.insurance.insurance.service;


import com.insurance.insurance.dto.AutoRequestDTO;
import com.insurance.insurance.dto.FireRequestDTO;
import com.insurance.insurance.dto.HealthInsuranceDTO;
import com.insurance.insurance.dto.HealthRequestDTO;
import com.insurance.insurance.entity.*;
import com.insurance.insurance.exception.DataNotFoundException;
import com.insurance.insurance.repository.RequestRepository;
import com.insurance.insurance.repository.UserInfoRepository;
import com.insurance.insurance.util.MultipartUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final OCRService ocrService;
    private final ChatGPTService chatGPTService;
    private final InsuranceService insuranceService;
    private final TransactionService transactionService;

    @Transactional
    public CompletableFuture<Void> requestHealth(SiteUser siteUser, HealthRequestDTO healthRequestDTO,Integer id, List<byte[]> receipts, List<byte[]> additionalDocuments){
        return CompletableFuture.runAsync(() -> {

            String content = healthRequestDTO.getContent();
            int price = healthRequestDTO.getPrice();
            String date = healthRequestDTO.getDate().toString();
            String claimType = healthRequestDTO.getClaimType();
            String hospitalName = healthRequestDTO.getHospitalName();

            HealthInsurance healthInsurance = insuranceService.getHealthBySiteUserAndId(siteUser, id);
            RiskRank riskRank = healthInsurance.getRiskRank();
            String description = riskRank.getDescription();
            //request 생성
            LocalDateTime requestDate = LocalDateTime.now();
            Request request = new Request(siteUser,healthInsurance, claimType, content, price, requestDate, "pending");
            request = requestRepository.save(request);

            // OCR 처리
            List<String> receiptTexts = receipts.stream()
                    .map(receipt -> {
                        try {
                            return ocrService.extractText(receipt);
                        } catch (IOException e) {
                            throw new RuntimeException("OCR 처리 실패: " + e.getMessage(), e);
                        }
                    })
                    .toList();
            MultipartUtil.cleanupFiles(receipts);

            List<String> additionalDocumentTexts = additionalDocuments.stream()
                    .filter(document -> document != null && document.length > 0)
                    .map(document -> {
                        try {
                            return ocrService.extractText(document);
                        } catch (IOException e) {
                            throw new RuntimeException("OCR 처리 실패: " + e.getMessage(), e);
                        }
                    })
                    .toList();
            MultipartUtil.cleanupFiles(additionalDocuments);

            //메시지검증
            String userMessage = GPTHealthMessage(content, price, receiptTexts, date, claimType, hospitalName, additionalDocumentTexts, description);
            System.out.println("메시지 : " + userMessage);
            String responseMessage = "";
            try {
                responseMessage = chatGPTService.getChatGPTResponse(userMessage);
            } catch (Exception e) {
                //throw
            }
            if (responseMessage.equals("true")) {
                request.setStatus("approved");
                request.setDescription("검토 결과, 보험금은 지급이 가능합니다.");
            } else {
                request.setStatus("rejected");
                System.out.println("답변 : " + responseMessage);
                request.setDescription(responseMessage);
            }
            request = requestRepository.save(request);


            Integer deductible_rate = riskRank.getDeductible_rate();
            Integer coverage_limit = riskRank.getCoverage_limit();
            price = (int) Math.min(price * (deductible_rate / 100.0), coverage_limit * price);

            Transaction transaction = transactionService.transaction(siteUser, request, price);
        }).exceptionally(ex -> {
            System.err.println("Error during Request: " + ex.getMessage());
            return null;
        });
    }


    @Transactional
    public CompletableFuture<Void> requestFire(SiteUser siteUser, FireRequestDTO fireRequestDTO, Integer id, List<byte[]> receipts,List<byte[]> incidentReports, List<byte[]> additionalDocuments) {
        return CompletableFuture.runAsync(() -> {
            UserInfo userInfo = siteUser.getUserInfo();
            //dto정보들 받아오기
            String content = fireRequestDTO.getContent();
            String date = fireRequestDTO.getDate().toString();
            int price = fireRequestDTO.getPrice();
            String damageType = fireRequestDTO.getDamageType();
            FireInsurance fireInsurance = insuranceService.getFireBySiteUserAndId(siteUser,id);
            RiskRank riskRank = fireInsurance.getRiskRank();
            String description = riskRank.getDescription();
            //request 생성
            LocalDateTime requestDate = LocalDateTime.now();
            Request request = new Request(siteUser,fireInsurance,damageType,content,price,requestDate,"pending");
            request= requestRepository.save(request);

            // OCR 처리
            List<String> receiptTexts = receipts.stream()
                    .map(receipt -> {
                        try {
                            return ocrService.extractText(receipt);
                        } catch (IOException e) {
                            throw new RuntimeException("OCR 처리 실패: " + e.getMessage(), e);
                        }
                    })
                    .toList();
            MultipartUtil.cleanupFiles(receipts);

            List<String> incidentReportTexts = incidentReports.stream()
                    .filter(document -> document != null && document.length > 0)
                    .map(document -> {
                        try {
                            return ocrService.extractText(document);
                        } catch (IOException e) {
                            throw new RuntimeException("OCR 처리 실패: " + e.getMessage(), e);
                        }
                    })
                    .toList();
            MultipartUtil.cleanupFiles(incidentReports);

            List<String> additionalDocumentTexts = additionalDocuments.stream()
                    .filter(document -> document != null && document.length > 0)
                    .map(document -> {
                        try {
                            return ocrService.extractText(document);
                        } catch (IOException e) {
                            throw new RuntimeException("OCR 처리 실패: " + e.getMessage(), e);
                        }
                    })
                    .toList();
            MultipartUtil.cleanupFiles(additionalDocuments);
            //메시지검증
            String userMessage = GPTFireMessage(content, receiptTexts, date, price, damageType, incidentReportTexts, additionalDocumentTexts, description);
            String responseMessage = "";
            try {
                responseMessage = chatGPTService.getChatGPTResponse(userMessage);
            } catch (Exception e) {
                //throw
            }
            if (responseMessage.equals("true")){
                request.setStatus("approved");
            }
            else{
                request.setStatus("rejected");
                request.setDescription(responseMessage);
            }

            request = requestRepository.save(request);
            Integer deductible_rate = riskRank.getDeductible_rate();
            Integer coverage_limit = riskRank.getCoverage_limit();
            price = (int) Math.min(price * (deductible_rate / 100.0), coverage_limit * price);
            Transaction transaction = transactionService.transaction(siteUser, request,price);


        }).exceptionally(ex -> {
            System.err.println("Error during Request: " + ex.getMessage());
            return null;
        });

    }


    @Transactional
    public CompletableFuture<Void> requestAuto(SiteUser siteUser, AutoRequestDTO autoRequestDTO, Integer id,List<byte[]> receipts,List<byte[]> policeReports,List<byte[]> additionalDocuments) {
        return CompletableFuture.runAsync(() -> {
            UserInfo userInfo = siteUser.getUserInfo();
            //dto정보들 받아오기
            String content = autoRequestDTO.getContent();
            String date = autoRequestDTO.getDate().toString();
            int price = autoRequestDTO.getPrice();

            String damageType = autoRequestDTO.getDamageType();
            String accidentLocation = autoRequestDTO.getAccidentLocation();

            AutoInsurance autoInsurance = insuranceService.getAutoBySiteUserAndId(siteUser,id);
            RiskRank riskRank = autoInsurance.getRiskRank();
            String description = riskRank.getDescription();

            //request 생성
            LocalDateTime requestDate = LocalDateTime.now();
            Request request = new Request(siteUser,autoInsurance,damageType,content,price,requestDate,"pending");
            request= requestRepository.save(request);


            // OCR 처리
            List<String> receiptTexts = receipts.stream()
                    .map(receipt -> {
                        try {
                            return ocrService.extractText(receipt);
                        } catch (IOException e) {
                            throw new RuntimeException("OCR 처리 실패: " + e.getMessage(), e);
                        }
                    })
                    .toList();
            MultipartUtil.cleanupFiles(receipts);


            List<String> policeReportTexts = policeReports.stream()
                    .filter(document -> document != null && document.length > 0)
                    .map(document -> {
                        try {
                            return ocrService.extractText(document);
                        } catch (IOException e) {
                            throw new RuntimeException("OCR 처리 실패: " + e.getMessage(), e);
                        }
                    })
                    .toList();
            MultipartUtil.cleanupFiles(policeReports);

            List<String> additionalDocumentTexts = additionalDocuments.stream()
                    .filter(document -> document != null && document.length > 0)
                    .map(document -> {
                        try {
                            return ocrService.extractText(document);
                        } catch (IOException e) {
                            throw new RuntimeException("OCR 처리 실패: " + e.getMessage(), e);
                        }
                    })
                    .toList();
            MultipartUtil.cleanupFiles(additionalDocuments);

            //메시지검증
            String userMessage = GPTAutoMessage(content, price,receiptTexts, date, damageType, accidentLocation,policeReportTexts, additionalDocumentTexts, description);
            String responseMessage = "";
            try {
                responseMessage = chatGPTService.getChatGPTResponse(userMessage);
            } catch (Exception e) {
                //throw
            }
            if (responseMessage.equals("true")){
                request.setStatus("approved");
            }
            else{
                request.setStatus("rejected");
                request.setDescription(responseMessage);
            }
            request = requestRepository.save(request);
            Integer deductible_rate = riskRank.getDeductible_rate();
            Integer coverage_limit = riskRank.getCoverage_limit();
            price = (int) Math.min(price * (deductible_rate / 100.0), coverage_limit * price);
            Transaction transaction = transactionService.transaction(siteUser, request,price);


        }).exceptionally(ex -> {
            System.err.println("Error during Request: " + ex.getMessage());
            return null;
        });
    }

    @Transactional
    private String GPTFireMessage(String content, List<String> receipts, String date, int price, String damageType, List<String> incidentReports, List<String> additionalDocuments, String description) {
        StringBuilder message = new StringBuilder();

        message.append("영수증내용: ");
        int i = 0;
        for (String receipt : receipts) {
            i++;
            message.append("\n영수증사진").append(i).append(": ").append(receipt);
        }


        message.append("\n--- 수기 작성 내용 ---")
                .append("\n사고날짜: ").append(date)
                .append("\n가격: ").append(price)
                .append("\n청구유형: ").append(damageType)
                .append("\n청구내용: ").append(content);

        i = 0;
        for (String incidentReport : incidentReports) {
            i++;
            message.append("\n화재 사고 보고서").append(i).append(": ").append(incidentReport);
        }

        if (additionalDocuments != null) {
            i = 0;
            for (String additionalDocument : additionalDocuments) {
                i++;
                message.append("\n추가서류").append(i).append(": ").append(additionalDocument);
            }
        }else {
            message.append("\n추가서류 없음");
        }

        message.append("\n보험약관: ").append(description)
                .append("\n\n다음 데이터를 검토하여 보험비 청구 가능 여부를 판단하세요:\n\n")
                .append("1. 전반적인 내용이 화재 보험 청구 내용인지 확인.\n")
                .append("2. 영수증 내용과 수기로 입력된 내용이 일치하는지 확인.\n")
                .append("3. 청구 유형, 청구 내용, 화재 사고 보고서가 서로 합당한지 확인.\n")
                .append("4. 청구 내용, 화재 사고 보고서, 추가 서류가 보험 가입 내용에 따라 보험비를 지급받을 수 있는지 확인.\n")
                .append("5. 화재 사고 보고서, 추가 서류, 사고날짜가 알맞는지 확인.\n")
                .append("6. 보험 약관에 따른 보장 항목인지 확인.\n")
                .append("7. 청구 내용이 중복되거나 의도적으로 부풀려진 사항은 없는지 검토하세요.\n")
                .append("8. 제출된 정보가 불충분하거나 모호한 경우, 고객에게 추가 설명이나 서류를 요청할 수 있습니다.\n\n")
                .append("참고: 영수증, 화재 사고 보고서, 추가 서류는 OCR로 여러 사진에서 추출된 데이터입니다. ")
                .append("따라서 텍스트 내용이 정확하지 않거나 중복될 가능성이 있으니 유의하여 검토하세요.\n\n")
                .append("검증 결과:\n")
                .append("- 가장중요 : 보험비를 지급할 수 있다면 앞뒤 내용 사족 붙이지말고 절대 아무것도 없이 무조건 true 만 출력하세요.\n")
                .append("- 보험비를 받을 수 없다면 그 이유를 3줄 이내로 작성하고, 고객에게 전달할 형식으로 존댓말로 작성하세요.\n")
                .append("- 보험비 받을 수 있는지 검사는 확실하게 해주세요.");

        return message.toString();
    }

    @Transactional
    private String GPTHealthMessage(String content, int price, List<String> receipts, String date, String claimType, String hospitalName, List<String> additionalDocuments, String description) {
        StringBuilder message = new StringBuilder();

        message.append("영수증 내용: ");
        int i = 0;
        for (String receipt : receipts) {
            i++;
            message.append("\n영수증 사진").append(i).append(": ").append(receipt);
        }
        System.out.println(message.toString());
        message.append("\n--- 수기 작성 내용 ---")
                .append("\n날짜: ").append(date)
                .append("\n가격: ").append(price).append("원")
                .append("\n병원 이름: ").append(hospitalName)
                .append("\n청구 유형: ").append(claimType)
                .append("\n청구 내용: ").append(content);

        if (additionalDocuments != null) {
            i = 0;
            for (String additionalDocument : additionalDocuments) {
                i++;
                message.append("\n추가 서류 내용").append(i).append(": ").append(additionalDocument);
            }
        } else {
            message.append("\n추가 서류 없음");
        }

        message.append("\n보험 가입 내용: ").append(description)
                .append("\n\n검토해야 할 사항은 다음과 같습니다:\n")
                .append("1. 전반적인 내용이 건강 보험 청구 내용인지 확인.\n")
                .append("2. 영수증 내용을 통해 수기 작성 내용의 날짜, 가격, 병원 이름을 정확히 입력되었는지 확인하세요.\n")
                .append("3. 청구 유형과 청구 내용이 서로 합당한지 확인하세요.\n")
                .append("4. 청구 내용과 추가 서류를 검토하여 보험 가입 내용에 따라 보험비를 지급받을 수 있는지 판단하세요.\n")
                .append("5. 보험 약관에서 보장 항목에 해당하는지 확인.\n")
                .append("6. 청구 내용이 중복되거나 과장된 사항은 없는지 검토하세요.\n")
                .append("7. 필요한 서류가 제출되지 않았거나 정보가 부족하다면 고객에게 추가 설명이나 서류를 요청하세요.\n\n")
                .append("참고: 영수증, 추가 서류는 OCR로 여러 사진에서 추출된 데이터입니다. ")
                .append("따라서 텍스트 내용이 정확하지 않거나 중복될 가능성이 있으니 유의하여 검토하세요.\n\n")
                .append("검토 결과:\n")
                .append("- 가장중요 : 보험비를 지급할 수 있다면 앞뒤 내용 사족 붙이지말고 절대 아무것도 없이 무조건 true 만 출력하세요.\n")
                .append("- 지급할 수 없다면 이유를 3줄 이내로 작성하여, 고객에게 전달할 형식으로 존댓말로 작성해 주세요.\n")
                .append("- 보험비 받을 수 있는지 검사는 확실하게 해주세요.");

        return message.toString();
    }

    @Transactional
    private String GPTAutoMessage(String content, int price, List<String> receipts, String date, String damageType, String accidentLocation,List<String> policeReports, List<String> additionalDocuments, String description) {
        StringBuilder message = new StringBuilder();

        message.append("영수증내용: ");
        int i = 0;
        for (String receipt : receipts) {
            i++;
            message.append("\n영수증사진").append(i).append(": ").append(receipt);
        }
        System.out.println(message.toString());

        message.append("\n--- 수기 작성 내용 ---")
                .append("\n사고날짜: ").append(date)
                .append("\n가격: ").append(price)
                .append("\n청구유형: ").append(damageType)
                .append("\n청구내용: ").append(content)
                .append("\n사고 발생 장소 : " +accidentLocation);

        i = 0;
        for (String incidentReport : policeReports) {
            i++;
            message.append("\n사고 경위서").append(i).append(": ").append(incidentReport);
        }

        if (additionalDocuments != null) {
            i = 0;
            for (String additionalDocument : additionalDocuments) {
                i++;
                message.append("\n추가서류").append(i).append(": ").append(additionalDocument);
            }
        }else {
            message.append("\n추가서류 없음");
        }


        message.append("\n보험약관: ").append(description)
                .append("\n\n다음 데이터를 검토하여 보험비 청구 가능 여부를 판단하세요:\n\n")
                .append("1. 전반적인 내용이 자동차 보험 청구 내용인지 확인.\n")
                .append("2. 영수증 내용과 수기 작성된 내용이 일치하는지 확인.\n")
                .append("3. 청구 유형, 청구 내용, 사고 경위서가 서로 합당한지 확인.\n")
                .append("4. 청구 내용, 사고 경위서, 추가 서류가 보험 가입 내용에 따라 보험비를 지급받을 수 있는지 확인.\n")
                .append("5. 사고 경위서, 추가 서류에 대해 사고 발생 장소, 사고날짜가 알맞는지 확인.\n")
                .append("6. 보험 약관에 따른 보장 항목인지 확인.\n")
                .append("7. 청구 내용이 중복되거나 의도적으로 부풀려진 사항은 없는지 검토하세요.\n")
                .append("8. 제출된 정보가 불충분하거나 모호한 경우, 고객에게 추가 설명이나 서류를 요청할 수 있습니다.\n\n")
                .append("참고: 영수증, 사고 경위서, 추가 서류는 OCR로 여러 사진에서 추출된 데이터입니다. ")
                .append("따라서 텍스트 내용이 정확하지 않거나 중복될 가능성이 있으니 유의하여 검토하세요.\n\n")
                .append("검증 결과:\n")
                .append("- 가장중요 : 보험비를 지급할 수 있다면 앞뒤 내용 사족 붙이지말고 절대 아무것도 없이 무조건 true 만 출력하세요.\n")
                .append("- 보험비를 받을 수 없다면 그 이유를 3줄 이내로 작성하고, 고객에게 전달할 형식으로 존댓말로 작성하세요.\n")
                .append("- 보험비 받을 수 있는지 검사는 확실하게 해주세요.");

        return message.toString();
    }



    public List<Request> getBySiteUser(SiteUser siteUser) {
        return requestRepository.findAllBySiteUser(siteUser);
    }

    public Page<Request> getBySiteUser(SiteUser siteUser, Pageable pageable) {
        return requestRepository.findAllBySiteUser(siteUser, pageable);
    }

    public Request getBySiteUserAndId(SiteUser siteUser, Integer id) {
        return requestRepository.findBySiteUserAndId(siteUser,id);
    }





}
