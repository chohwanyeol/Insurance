package com.insurance.insurance.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;


public class ResponseUtil {

    public static <T> ResponseEntity<Map<String, T>> createErrorResponse(HttpStatus status, T message) {
        return ResponseEntity.status(status).body(Map.of("message", message));
    }

    public static <T> ResponseEntity<Map<String, T>> createSuccessResponse(String key, T value) {
        return ResponseEntity.ok(Map.of(key, value));
    }
}