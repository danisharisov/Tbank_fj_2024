package com.example.fj_2024_lesson_5;

import com.example.fj_2024_lesson_5.entity.Location;
import com.example.fj_2024_lesson_5.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.*;

@SpringBootTest
@Testcontainers
public class LocationIntegrationTest {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    void testCreateLocation() {
        Location location = new Location();
        location.setName("New York");

        Location savedLocation = locationRepository.save(location);

        assertNotNull(savedLocation.getId());
        assertEquals("New York", savedLocation.getName());
    }

    @Test
    void testFindLocationById() {
        Location location = new Location();
        location.setName("Paris");

        Location savedLocation = locationRepository.save(location);

        Optional<Location> foundLocation = locationRepository.findById(savedLocation.getId());
        assertTrue(foundLocation.isPresent());
        assertEquals("Paris", foundLocation.get().getName());
    }

    @Test
    void testDeleteLocation() {
        Location location = new Location();
        location.setName("Tokyo");

        Location savedLocation = locationRepository.save(location);
        locationRepository.deleteById(savedLocation.getId());

        Optional<Location> deletedLocation = locationRepository.findById(savedLocation.getId());
        assertFalse(deletedLocation.isPresent());
    }

    @BeforeEach
    public void cleanUp() {
        locationRepository.deleteAll();
    }
}

