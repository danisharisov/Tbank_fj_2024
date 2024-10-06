package com.example.fj_2024_lesson_5.controllers;

import com.example.fj_2024_lesson_5.dto.Location;
import com.example.fj_2024_lesson_5.exceptions.LocationNotFoundException;
import com.example.fj_2024_lesson_5.services.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    @Test
    void testGetAllLocations_Positive() throws Exception {
        List<Location> locations = Arrays.asList(
                new Location("slug1", "Location1"),
                new Location("slug2", "Location2")
        );
        when(locationService.getAllLocations()).thenReturn(locations);

        mockMvc.perform(get("/api/v1/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))  // Проверяем, что возвращается 2 объекта
                .andExpect(jsonPath("$[0].slug").value("slug1"))
                .andExpect(jsonPath("$[0].name").value("Location1"));
    }

    @Test
    void testGetLocationBySlug_Positive() throws Exception {
        Location location = new Location("slug1", "Location1");
        when(locationService.getLocationBySlug("slug1")).thenReturn(location);

        mockMvc.perform(get("/api/v1/locations/slug1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value("slug1"))
                .andExpect(jsonPath("$.name").value("Location1"));
    }

    @Test
    void testGetLocationBySlug_NotFound() throws Exception {
        when(locationService.getLocationBySlug("unknown_slug")).thenThrow(new LocationNotFoundException("Location not found"));

        mockMvc.perform(get("/api/v1/locations/unknown_slug"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Location not found")));
    }
}
