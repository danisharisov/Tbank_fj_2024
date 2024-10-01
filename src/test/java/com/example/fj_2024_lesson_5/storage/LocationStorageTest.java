package com.example.fj_2024_lesson_5.storage;

import com.example.fj_2024_lesson_5.dto.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocationStorageTest {

    private LocationStorage locationStorage;

    @BeforeEach
    void setUp() {
        locationStorage = new LocationStorage();
    }

    @Test
    void testSaveLocation() {
        Location location = new Location("ekb", "Ekaterinburg");
        locationStorage.save("ekb", location);

        assertEquals(location, locationStorage.findById("ekb"));
    }

    @Test
    void testFindById_Found() {
        Location location = new Location("ekb", "Ekaterinburg");
        locationStorage.save("ekb", location);
        Location result = locationStorage.findById("ekb");
        assertNotNull(result);
        assertEquals("Ekaterinburg", result.getName());
    }

    @Test
    void testFindById_NotFound() {
        assertNull(locationStorage.findById("unknown_slug"));
    }

    @Test
    void testFindAllLocations() {
        Location location1 = new Location("ekb", "Ekaterinburg");
        Location location2 = new Location("msk", "Moscow");
        locationStorage.save("ekb", location1);
        locationStorage.save("msk", location2);
        Collection<Location> locations = locationStorage.findAll();
        List<Location> locationList = locations.stream().toList();

        assertEquals(2, locationList.size());
        assertTrue(locationList.contains(location1));
        assertTrue(locationList.contains(location2));
    }

    @Test
    void testDeleteLocation() {
        Location location = new Location("ekb", "Ekaterinburg");
        locationStorage.save("ekb", location);

        locationStorage.delete("ekb");
        assertNull(locationStorage.findById("ekb"));
    }
}
