package com.example.fj_2024_lesson_5.services;

import com.example.fj_2024_lesson_5.dto.Location;
import com.example.fj_2024_lesson_5.exceptions.EntityAlreadyExistsException;
import com.example.fj_2024_lesson_5.exceptions.LocationNotFoundException;
import com.example.fj_2024_lesson_5.storage.LocationStorage;
import com.example.fj_2024_lesson_5.timed.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
@Service
@RequiredArgsConstructor
public class LocationService {
    @Value("${kudago.location.api.url}")
    private String locationUrl;
    private final RestTemplate restTemplate;
    private final LocationStorage locationStorage;
    @Timed
    public void fetchLocationsFromKudaGo() {
        Location[] locations = restTemplate.getForObject(locationUrl, Location[].class);
        if (locations != null) {
            saveAll(List.of(locations));
        }
    }

    public List<Location> getAllLocations() {
        return List.copyOf(locationStorage.findAll());
    }

    public Location getLocationBySlug(String slug) {
        Location location = locationStorage.findById(slug);
        if (location == null) {
            throw new LocationNotFoundException("Location with slug " + slug + " not found");
        }
        return location;
    }

    public void createLocation(Location location) {
        if (locationStorage.findById(location.getSlug()) != null) {
            throw new EntityAlreadyExistsException("Location with slug " + location.getSlug() + " already exists.");
        }
        locationStorage.save(location.getSlug(), location);
    }
    public void updateLocation(String slug, Location updatedLocation) {
        Location existingLocation = locationStorage.findById(slug);
        if (existingLocation == null) {
            throw new LocationNotFoundException("Location with slug " + slug + " not found");
        }
        updatedLocation.setSlug(slug);
        locationStorage.update(slug, updatedLocation);
    }

    public void deleteLocation(String slug) {
        if (locationStorage.findById(slug) == null) {
            throw new LocationNotFoundException("Location with slug " + slug + " not found");
        }
        locationStorage.delete(slug);
    }

    public void saveAll(List<Location> locations) {
        locations.forEach(location -> locationStorage.save(location.getSlug(), location));
    }

}