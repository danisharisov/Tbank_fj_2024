package com.example.fj_2024_lesson_5.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LocationClient implements ApiClient<KudaGoLocationResponse> {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kudago.location.api.url}")
    public String BASE_URL;

    @Override
    public List<KudaGoLocationResponse> getAllEntitiesFromKudaGo() {
        try {
            String jsonResponse = restTemplate.getForObject(BASE_URL, String.class);
            return objectMapper.readValue(jsonResponse, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error with kudago api request: " + e.getMessage(), e);
        }
    }
}
