package com.example.fj_2024_lesson_5.services;

import com.example.fj_2024_lesson_5.dto.Location;
import com.example.fj_2024_lesson_5.exceptions.EntityAlreadyExistsException;
import com.example.fj_2024_lesson_5.exceptions.LocationNotFoundException;
import com.example.fj_2024_lesson_5.storage.LocationStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocationServiceTest {

    @Mock
    private LocationStorage locationStorage;

    @InjectMocks
    private LocationService locationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLocations_PositiveScenario() {
        List<Location> mockLocations = Arrays.asList(new Location("slug1", "Location1"), new Location("slug2", "Location2"));
        when(locationStorage.findAll()).thenReturn(mockLocations);

        List<Location> result = locationService.getAllLocations();

        assertEquals(2, result.size());
        assertEquals("Location1", result.get(0).getName());
    }

    @Test
    void testGetLocationBySlug_PositiveScenario() {
        Location mockLocation = new Location("slug1", "Location1");
        when(locationStorage.findById("slug1")).thenReturn(mockLocation);

        Location result = locationService.getLocationBySlug("slug1");

        assertNotNull(result);
        assertEquals("Location1", result.getName());
    }

    @Test
    void testGetLocationBySlug_NegativeScenario() {
        when(locationStorage.findById("unknown_slug")).thenReturn(null);

        assertThrows(LocationNotFoundException.class, () -> locationService.getLocationBySlug("unknown_slug"));
    }

    @Test
    void testCreateLocation_Success() {
        Location newLocation = new Location("slug1", "Location1");

        locationService.createLocation(newLocation);

        verify(locationStorage, times(1)).save(newLocation.getSlug(), newLocation);
    }

    @Test
    void testCreateLocation_EntityAlreadyExistsScenario() {
        Location existingLocation = new Location("slug1", "Location1");
        when(locationStorage.findById("slug1")).thenReturn(existingLocation);

        assertThrows(EntityAlreadyExistsException.class, () -> locationService.createLocation(existingLocation));
    }

    @Test
    void testUpdateLocation_Success() {
        Location existingLocation = new Location("slug1", "Location1");
        when(locationStorage.findById("slug1")).thenReturn(existingLocation);

        Location updatedLocation = new Location("slug1", "Updated Location");
        locationService.updateLocation("slug1", updatedLocation);

        verify(locationStorage, times(1)).update("slug1", updatedLocation);
    }

    @Test
    void testUpdateLocation_NotFound() {
        when(locationStorage.findById("unknown_slug")).thenReturn(null);
        Location updatedLocation = new Location("unknown_slug", "Updated Location");

        assertThrows(LocationNotFoundException.class, () -> locationService.updateLocation("unknown_slug", updatedLocation));
    }

    @Test
    void testDeleteLocation_Success() {
        Location existingLocation = new Location("slug1", "Location1");
        when(locationStorage.findById("slug1")).thenReturn(existingLocation);

        locationService.deleteLocation("slug1");

        verify(locationStorage, times(1)).delete("slug1");
    }

    @Test
    void testDeleteLocation_NotFound() {
        when(locationStorage.findById("unknown_slug")).thenReturn(null);

        assertThrows(LocationNotFoundException.class, () -> locationService.deleteLocation("unknown_slug"));
    }
}
