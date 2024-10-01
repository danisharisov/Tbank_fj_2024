package com.example.fj_2024_lesson_5.controllers;

import com.example.fj_2024_lesson_5.dto.Location;
import com.example.fj_2024_lesson_5.exceptions.EntityAlreadyExistsException;
import com.example.fj_2024_lesson_5.exceptions.LocationNotFoundException;
import com.example.fj_2024_lesson_5.services.LocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LocationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Location testLocation1;
    private Location testLocation2;

    private static final String BASE_URL = "/api/v1/locations";

    @BeforeEach
    public void setup() {
        testLocation1 = new Location("ekb", "Ekaterinburg");
        testLocation2 = new Location("msk", "Moscow");
    }

    @Test
    public void testGetAllLocations() throws Exception {
        List<Location> mockLocations = Arrays.asList(testLocation1, testLocation2);
        Mockito.when(locationService.getAllLocations()).thenReturn(mockLocations);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Ekaterinburg"))
                .andExpect(jsonPath("$[1].name").value("Moscow"));
    }

    @Test
    public void testGetLocationBySlug_Success() throws Exception {
        Mockito.when(locationService.getLocationBySlug("ekb")).thenReturn(testLocation1);

        mockMvc.perform(get(BASE_URL + "/ekb"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Ekaterinburg"))
                .andExpect(jsonPath("$.slug").value("ekb"));
    }

    @Test
    public void testGetLocationBySlug_NotFound() throws Exception {
        Mockito.when(locationService.getLocationBySlug("unknown")).thenThrow(new LocationNotFoundException("Location not found"));

        mockMvc.perform(get(BASE_URL + "/unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateLocation_Success() throws Exception {
        Mockito.doNothing().when(locationService).createLocation(any(Location.class));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLocation1)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateLocation_Conflict() throws Exception {
        Mockito.doThrow(new EntityAlreadyExistsException("Location already exists")).when(locationService).createLocation(any(Location.class));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLocation1)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testUpdateLocation_Success() throws Exception {
        // Обновление с передачей slug и объекта Location
        Mockito.doNothing().when(locationService).updateLocation(eq("ekb"), any(Location.class));

        mockMvc.perform(put(BASE_URL + "/ekb")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLocation1)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateLocation_NotFound() throws Exception {
        Mockito.doThrow(new LocationNotFoundException("Location not found")).when(locationService).updateLocation(eq("unknown"), any(Location.class));

        mockMvc.perform(put(BASE_URL + "/unknown")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLocation1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteLocation_Success() throws Exception {
        Mockito.doNothing().when(locationService).deleteLocation("ekb");

        mockMvc.perform(delete(BASE_URL + "/ekb"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteLocation_NotFound() throws Exception {
        Mockito.doThrow(new LocationNotFoundException("Location not found")).when(locationService).deleteLocation("unknown");

        mockMvc.perform(delete(BASE_URL + "/unknown"))
                .andExpect(status().isNotFound());
    }
}