package com.insurance.insurance.service;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class OCRService {
    public String extractText(MultipartFile file) throws IOException {
        // Tesseract 설정
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("tessdata"); // Tesseract 데이터 경로
        tesseract.setLanguage("kor+eng"); // 언어 설정: 한글과 영어 지원

        // 파일 저장 (임시)
        File tempFile = File.createTempFile("receipt", ".png");
        file.transferTo(tempFile);

        try {
            // OCR 수행
            return tesseract.doOCR(tempFile);
        } catch (TesseractException e) {
            throw new RuntimeException("OCR 실패: " + e.getMessage());
        } finally {
            tempFile.delete(); // 임시 파일 삭제
        }
    }
}
