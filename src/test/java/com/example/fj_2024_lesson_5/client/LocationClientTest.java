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
        "kudago.location-url=http://localhost:8080/api/v1/locations"
})
public class LocationClientTest {

    @Container
    private static final GenericContainer<?> wireMockContainer = new GenericContainer<>(DockerImageName.parse("wiremock/wiremock:3.6.0"))
            .withExposedPorts(8080)
            .withCommand("--local-response-templating --verbose --port 8080");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("kudago.location.api.url", () -> "http://localhost:" + wireMockContainer.getMappedPort(8080) + "/api/v1/locations");
    }

    @Autowired
    private LocationClient locationClient;

    private static RestTemplate restTemplate = new RestTemplate();

    @BeforeAll
    public static void setUpWireMock() throws Exception {
        String baseUrl = "http://localhost:" + wireMockContainer.getMappedPort(8080);
        System.out.println("WireMock Base URL: " + baseUrl);

        ClassLoader classLoader = LocationClientTest.class.getClassLoader();
        File locationFile = new File(classLoader.getResource("location-mapping.json").getFile());
        String locationMapping = new String(Files.readAllBytes(locationFile.toPath()));

        System.out.println("Location Mapping JSON Content: " + locationMapping);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(locationMapping, headers);

        var response = restTemplate.postForEntity(baseUrl + "/__admin/mappings", request, String.class);

        System.out.println("WireMock response status for location mapping: " + response.getStatusCodeValue());
        System.out.println("WireMock response for location mapping: " + response.getBody());

        assertEquals(201, response.getStatusCodeValue(), "WireMock mapping creation failed for locations");

        var mappings = restTemplate.getForObject(baseUrl + "/__admin/mappings", String.class);
        System.out.println("Registered WireMock mappings: " + mappings);
    }

    @AfterAll
    public static void tearDown() {
        wireMockContainer.stop();
    }

    @Test
    public void testGetAllLocations_ShouldReturnLocations() {
        List<KudaGoLocationResponse> locations = locationClient.getAllEntitiesFromKudaGo();
        assertNotNull(locations);
        assertEquals(2, locations.size());
        assertEquals("Moscow", locations.get(0).getName());
    }
}
