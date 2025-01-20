package com.insurance.insurance.service;

import jakarta.transaction.Transactional;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

@Service
public class OCRService {

    private final Tika tika = new Tika(); // Apache Tika로 파일 형식 판별

    @Transactional
    public String extractText(byte[] fileBytes) throws IOException {
        if (fileBytes == null || fileBytes.length == 0) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("tessdata");
        tesseract.setLanguage("kor+eng");

        // 파일 데이터 기반 판별
        String mimeType = tika.detect(fileBytes);
        System.out.println("파일 MIME 타입: " + mimeType);

        // 임시 파일 생성
        Path tempFile = Files.createTempFile("ocr_", null);
        Files.write(tempFile, fileBytes);

        try {
            if (mimeType.equals("application/pdf")) {
                return extractTextFromPDF(tempFile.toFile(), tesseract);
            } else if (mimeType.startsWith("image/")) {
                return extractTextFromImage(tempFile.toFile(), tesseract);
            } else {
                throw new IllegalArgumentException("지원되지 않는 파일 형식입니다. PDF 또는 이미지 파일만 지원됩니다.");
            }
        } finally {
            // 임시 파일 삭제
            Files.deleteIfExists(tempFile);
        }
    }

    private String extractTextFromPDF(File pdfFile, Tesseract tesseract) throws IOException {
        StringBuilder result = new StringBuilder();
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 300); // DPI 설정
                try {
                    result.append(tesseract.doOCR(bufferedImage));
                } catch (TesseractException e) {
                    throw new RuntimeException("OCR 실패 (PDF 페이지): " + e.getMessage(), e);
                }
            }
        }
        return result.toString();
    }

    private String extractTextFromImage(File imageFile, Tesseract tesseract) throws IOException {
        try {
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            if (bufferedImage == null) {
                throw new IllegalArgumentException("유효하지 않은 이미지 파일입니다.");
            }
            return tesseract.doOCR(bufferedImage);
        } catch (TesseractException e) {
            throw new RuntimeException("OCR 실패 (이미지): " + e.getMessage(), e);
        }
    }
}
