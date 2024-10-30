package com.example.fj_2024_lesson_5.client;

import com.example.fj_2024_lesson_5.entity.Category;
import com.example.fj_2024_lesson_5.entity.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KudaGoClient {
    private final RestTemplate restTemplate;

    @Value("${kudago.category.api.url}")
    private String categoriesURL;

    @Value("${kudago.location.api.url}")
    private String locationsURL;

    public List<Category> getAllCategories() {
        try {
            return restTemplate.exchange(
                    categoriesURL,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Category>>() {}
            ).getBody();
        } catch (RestClientException e) {
            log.error("Error fetching categories from API: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Location> getAllLocations() {
        try {
            return restTemplate.exchange(
                    locationsURL,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Location>>() {}
            ).getBody();
        } catch (RestClientException e) {
            log.error("Error fetching locations from API: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
