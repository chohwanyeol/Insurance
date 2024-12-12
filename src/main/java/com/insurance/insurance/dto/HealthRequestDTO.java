package com.insurance.insurance.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class HealthRequestDTO {
    private LocalDateTime dateTime; // 비용 날짜
    private String content; // 청구 내용
    private Integer price;
    private MultipartFile receiptImage; // 영수증 이미지 첨부
    private String claimType; // 청구 유형(예: "입원", "수술", "약제비")
    private String hospitalName; // 병원명
    private List<MultipartFile> additionalDocuments; // 추가 서류 첨부
}
