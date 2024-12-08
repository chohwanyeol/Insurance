package com.insurance.insurance.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
public class HealthRequestDTO {
    private LocalDateTime dateTime;
    private String content;
    private int price;
    private MultipartFile receiptImage;
}
