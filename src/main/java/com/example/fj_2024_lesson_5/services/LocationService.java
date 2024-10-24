package com.example.fj_2024_lesson_5.services;

import com.example.fj_2024_lesson_5.entity.Location;
import com.example.fj_2024_lesson_5.exceptions.LocationNotFoundException;
import com.example.fj_2024_lesson_5.repository.LocationRepository;
import com.example.fj_2024_lesson_5.timed.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationService {
    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Location getLocationById(UUID id) {
        return locationRepository.findById(id).orElseThrow(() ->
                new LocationNotFoundException("Location not found with ID: " + id));
    }
    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }
    public Location updateLocation(UUID id, Location locationDetails) {
        Location location = locationRepository.findById(id).orElseThrow(() ->
                new LocationNotFoundException("Location not found with ID: " + id));

        location.setName(locationDetails.getName());
        return locationRepository.save(location);
    }

    public void deleteLocation(UUID id) {
        Location location = locationRepository.findById(id).orElseThrow(() ->
                new LocationNotFoundException("Location not found with ID: " + id));
        locationRepository.delete(location);
    }


}