package com.insurance.insurance.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChatGPTService {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "your_api_key_here"; // OpenAI API 키를 여기에 입력

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getChatGPTResponse(String userMessage) throws IOException {
        // 요청 본문 구성
        Map<String, Object> message = new HashMap<>();
        message.put("model", "gpt-4");
        message.put("messages", new Object[]{
                Map.of("role", "user", "content", userMessage)
        });
        message.put("max_tokens", 1000);
        message.put("temperature", 0.7);

        // 요청 본문을 JSON으로 변환
        String requestBody = objectMapper.writeValueAsString(message);

        // HTTP 요청 생성
        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                .build();

        // 요청 실행 및 응답 처리
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Map<String, Object> responseBody = objectMapper.readValue(response.body().string(), Map.class);
                return (String) ((Map) ((Map) responseBody.get("choices")).get(0)).get("content");
            } else {
                throw new RuntimeException("ChatGPT API 호출 실패: " + response.body().string());
            }
        }
    }
}
