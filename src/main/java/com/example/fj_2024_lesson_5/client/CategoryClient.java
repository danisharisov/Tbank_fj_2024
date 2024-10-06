package com.example.fj_2024_lesson_5.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryClient implements ApiClient<KudaGoCategoryResponse> {

    @Value("${kudago.category.api.url}")
    private String baseUrl;

    private static final String MSG_CLIENT_ERROR = "Error with kudago api request '%s'";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public List<KudaGoCategoryResponse> getAllEntitiesFromKudaGo() {
        try {
            String jsonResponse = restTemplate.getForObject(baseUrl, String.class);
            return objectMapper.readValue(jsonResponse, new TypeReference<List<KudaGoCategoryResponse>>() {});
        } catch (Exception e) {
            throw new RuntimeException(String.format(MSG_CLIENT_ERROR, baseUrl), e);
        }
    }
}
