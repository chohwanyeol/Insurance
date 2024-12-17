package com.insurance.insurance.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FireRequestDTO {
    private LocalDateTime dateTime; // 비용 발생 날짜
    private String content; // 청구 내용
    private Integer price; // 청구 금액
    private MultipartFile receiptImage; // 영수증 이미지 첨부
    private String damageType; // 손해 유형(예: "주택", "상업시설", "가재도구", "생명구조")
    private MultipartFile incidentReport; // 화재 사고 보고서 첨부
    private List<MultipartFile> additionalDocuments; // 추가 서류 첨부
}

