package com.example.fj_2024_lesson_5.services;

import com.example.fj_2024_lesson_5.client.KudaGoClient;
import com.example.fj_2024_lesson_5.entity.Location;
import com.example.fj_2024_lesson_5.exceptions.LocationNotFoundException;
import com.example.fj_2024_lesson_5.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationService {
    private final LocationRepository locationRepository;
    private final KudaGoClient kudaGoClient;
    private final LocationHistoryService locationHistoryService;

    @Transactional(readOnly = true)
    public Location getLocationById(UUID id) {
        return locationRepository.findById(id).orElseThrow(() ->
                new LocationNotFoundException("Location not found with ID: " + id));
    }
    public Location createLocation(Location location) {
        locationHistoryService.saveSnapshot(location);
        return locationRepository.save(location);
    }
    public Location updateLocation(UUID id, Location locationDetails) {
        Location location = locationRepository.findById(id).orElseThrow(() ->
                new LocationNotFoundException("Location not found with ID: " + id));
        locationHistoryService.saveSnapshot(location);
        location.setName(locationDetails.getName());
        return locationRepository.save(location);
    }

    public void deleteLocation(UUID id) {
        Location location = locationRepository.findById(id).orElseThrow(() ->
                new LocationNotFoundException("Location not found with ID: " + id));
        locationHistoryService.saveSnapshot(location);
        location.setName(null);
        locationRepository.save(location);
    }
    @Transactional(readOnly = true)
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public List<Location> fetchLocationsFromKudaGo() {
        List<Location> locations = kudaGoClient.getAllLocations();
        locationRepository.saveAll(locations);
        return locations;
    }

    public void restoreLocation(UUID id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException("Location not found with ID: " + id));
        locationHistoryService.restoreLocation(location);
        locationRepository.save(location);
    }

}