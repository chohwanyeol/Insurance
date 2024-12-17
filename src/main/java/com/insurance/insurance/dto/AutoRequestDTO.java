package com.insurance.insurance.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AutoRequestDTO {
    private LocalDateTime dateTime; // 비용 발생 날짜
    private String content; // 청구 내용
    private Integer price; // 청구 금액
    private MultipartFile receiptImage; // 영수증 이미지 첨부
    private String damageType; // 손해 유형(예: "대인", "대물", "차량 수리")
    private String accidentLocation; // 사고 발생 장소
    private MultipartFile policeReport; // 사고 경위서(경찰 보고서) 첨부
    private List<MultipartFile> additionalDocuments; // 추가 서류 첨부
}
