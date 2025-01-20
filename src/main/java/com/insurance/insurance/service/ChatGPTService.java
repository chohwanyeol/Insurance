package com.insurance.insurance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChatGPTService {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Dotenv dotenv = Dotenv.load(); // .env 파일 로드

    public String getChatGPTResponse(String userMessage) throws IOException {
        // .env 파일에서 API 키 가져오기
        String apiKey = dotenv.get("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("API 키를 찾을 수 없습니다. .env 파일을 확인하세요.");
        }

        // 요청 본문 구성
        Map<String, Object> message = new HashMap<>();
        message.put("model", "gpt-4o");
        message.put("messages", new Object[]{
                Map.of("role", "system", "content", "보험 전문가가 돼서 청구 확인 역할을 해줘"),
                Map.of("role", "user", "content", userMessage)
        });
        message.put("max_tokens", 1000);
        message.put("temperature", 0.7);

        // 요청 본문을 JSON으로 변환
        String requestBody = objectMapper.writeValueAsString(message);

        // HTTP 요청 생성
        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + apiKey) // API 키 사용
                .header("Content-Type", "application/json")
                .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                .build();

        // 요청 실행 및 응답 처리
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                // 응답 본문을 문자열로 저장
                String responseBodyString = response.body().string();

                // API 응답을 Map으로 변환
                Map<String, Object> responseBody = objectMapper.readValue(responseBodyString, Map.class);

                // `choices` 배열에서 첫 번째 요소의 `message`의 `content`를 가져옴
                var choices = (java.util.List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> firstChoice = choices.get(0);
                    Map<String, Object> messageContent = (Map<String, Object>) firstChoice.get("message");
                    return (String) messageContent.get("content");
                } else {
                    throw new RuntimeException("ChatGPT 응답에 'choices'가 비어 있습니다.");
                }
            } else {
                throw new RuntimeException("ChatGPT API 호출 실패: " + (response.body() != null ? response.body().string() : "응답 없음"));
            }
        }
    }
}
