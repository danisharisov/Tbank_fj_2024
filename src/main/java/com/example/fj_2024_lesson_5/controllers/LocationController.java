package com.example.fj_2024_lesson_5.controllers;

import com.example.fj_2024_lesson_5.dto.Location;
import com.example.fj_2024_lesson_5.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {
    private final LocationService locationService;
    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Location> getLocationBySlug(@PathVariable String slug) {
        Location location = locationService.getLocationBySlug(slug);
        return ResponseEntity.ok(location);
    }

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        locationService.createLocation(location);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{slug}")
    public ResponseEntity<Location> updateLocation(@PathVariable String slug, @RequestBody Location updatedLocation) {
        locationService.updateLocation(slug, updatedLocation);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> deleteLocation(@PathVariable String slug) {
        locationService.deleteLocation(slug);
        return ResponseEntity.noContent().build();
    }
}