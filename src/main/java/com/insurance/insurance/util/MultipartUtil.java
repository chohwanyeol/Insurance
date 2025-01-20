package com.insurance.insurance.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MultipartUtil {
    private static final List<File> tempFiles = new ArrayList<>();

    // MultipartFile 리스트를 byte[] 리스트로 변환하고, 임시 파일로 저장
    public static List<byte[]> copyMultipartFiles(List<MultipartFile> multipartFiles) {
        List<byte[]> fileBytesList = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            try {
                byte[] fileBytes = file.getBytes();
                fileBytesList.add(fileBytes);

                // 임시 파일로 저장
                File tempFile = saveToTempFile(fileBytes);
                tempFiles.add(tempFile);
            } catch (IOException e) {
                throw new RuntimeException("파일 처리 중 오류 발생", e);
            }
        }

        return fileBytesList;
    }

    // byte[] 데이터를 임시 파일로 저장
    private static File saveToTempFile(byte[] data) throws IOException {
        File tempFile = Files.createTempFile("temp_file_", ".tmp").toFile();
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(data);
        }
        return tempFile;
    }

    // 특정 byte[] 리스트에 해당하는 임시 파일만 삭제
    public static void cleanupFiles(List<byte[]> byteDataList) {
        for (byte[] data : byteDataList) {
            tempFiles.removeIf(tempFile -> {
                try {
                    byte[] fileData = Files.readAllBytes(tempFile.toPath());
                    if (areBytesEqual(data, fileData)) {
                        Files.deleteIfExists(tempFile.toPath());
                        return true; // 삭제한 파일은 리스트에서 제거
                    }
                } catch (IOException e) {
                    System.err.println("임시 파일 삭제 중 오류 발생: " + e.getMessage());
                }
                return false;
            });
        }
    }

    // 모든 임시 파일 삭제
    public static void cleanupAllTempFiles() {
        for (File tempFile : tempFiles) {
            try {
                Files.deleteIfExists(tempFile.toPath());
            } catch (IOException e) {
                System.err.println("임시 파일 삭제 중 오류 발생: " + e.getMessage());
            }
        }
        tempFiles.clear();
    }

    // byte[] 데이터 비교 함수
    private static boolean areBytesEqual(byte[] array1, byte[] array2) {
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
}
