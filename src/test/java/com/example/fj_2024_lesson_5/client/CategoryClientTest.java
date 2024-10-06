package com.example.fj_2024_lesson_5.client;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest
@TestPropertySource(properties = {
        "kudago.category-url=http://localhost:8080/api/v1/places/categories"
})
public class CategoryClientTest {

    @Container
    private static final GenericContainer<?> wireMockContainer = new GenericContainer<>(DockerImageName.parse("wiremock/wiremock:3.6.0"))
            .withExposedPorts(8080)
            .withCommand("--local-response-templating --verbose --port 8080");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("kudago.category.api.url", () -> "http://localhost:" + wireMockContainer.getMappedPort(8080) + "/api/v1/places/categories");
    }

    @Autowired
    private CategoryClient categoryClient;

    private static RestTemplate restTemplate = new RestTemplate();

    @BeforeAll
    public static void setUpWireMock() throws Exception {
        String baseUrl = "http://localhost:" + wireMockContainer.getMappedPort(8080);
        System.out.println("WireMock Base URL: " + baseUrl);

        ClassLoader classLoader = CategoryClientTest.class.getClassLoader();
        File categoryFile = new File(classLoader.getResource("category-mapping.json").getFile());
        String categoryMapping = new String(Files.readAllBytes(categoryFile.toPath()));

        System.out.println("Category Mapping JSON Content: " + categoryMapping);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(categoryMapping, headers);

        var response = restTemplate.postForEntity(baseUrl + "/__admin/mappings", request, String.class);

        System.out.println("WireMock response status for category mapping: " + response.getStatusCodeValue());
        System.out.println("WireMock response for category mapping: " + response.getBody());

        assertEquals(201, response.getStatusCodeValue(), "WireMock mapping creation failed for categories");

        var mappings = restTemplate.getForObject(baseUrl + "/__admin/mappings", String.class);
        System.out.println("Registered WireMock mappings: " + mappings);
    }

    @AfterAll
    public static void tearDown() {
        wireMockContainer.stop();
    }

    @Test
    public void testGetAllCategories_ShouldReturnCategories() {
        List<KudaGoCategoryResponse> categories = categoryClient.getAllEntitiesFromKudaGo();
        assertNotNull(categories);
        assertEquals(2, categories.size());
        assertEquals("Category 1", categories.get(0).getName());
    }
}
