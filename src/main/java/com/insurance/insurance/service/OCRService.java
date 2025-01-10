package com.insurance.insurance.service;
import jakarta.transaction.Transactional;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;


@Service
public class OCRService {

    @Transactional
    public String extractText(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("tessdata");
        tesseract.setLanguage("kor+eng");

        // MultipartFile을 임시 디렉토리에 저장
        Path tempFile = Files.createTempFile("ocr_", "_" + file.getOriginalFilename());
        file.transferTo(tempFile.toFile());

        try {
            String extension = getFileExtension(file.getOriginalFilename());
            if ("pdf".equalsIgnoreCase(extension)) {
                return extractTextFromPDF(tempFile.toFile(), tesseract);
            } else {
                return extractTextFromImage(tempFile.toFile(), tesseract);
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

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
