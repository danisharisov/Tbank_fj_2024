package com.example.fj_2024_lesson_5.services;

import com.example.fj_2024_lesson_5.dto.Location;
import com.example.fj_2024_lesson_5.storage.LocationStorage;
import com.example.fj_2024_lesson_5.timed.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
@Timed
@Service
public class LocationService {
    private final RestTemplate restTemplate;
    private final LocationStorage locationStorage;

    @Autowired
    public LocationService(RestTemplate restTemplate, LocationStorage locationStorage) {
        this.restTemplate = restTemplate;
        this.locationStorage = locationStorage;
    }
    public void fetchLocationsFromKudaGo() {
        String url = "https://kudago.com/public-api/v1.4/locations";
        Location[] locations = restTemplate.getForObject(url, Location[].class);
        if (locations != null) {
            locationStorage.saveAll(List.of(locations));
        }
    }

    public List<Location> getAllLocations() {
        return List.copyOf(locationStorage.findAll());
    }

    public Location getLocationBySlug(String slug) {
        return locationStorage.findBySlug(slug);
    }

    public void createLocation(Location location) {
        locationStorage.save(location);
    }

    public void updateLocation(String slug, Location updatedLocation) {
        if (locationStorage.findBySlug(slug) != null) {
            updatedLocation.setSlug(slug);
            locationStorage.save(updatedLocation);
        }
    }

    public void deleteLocation(String slug) {
        locationStorage.deleteBySlug(slug);
    }
}