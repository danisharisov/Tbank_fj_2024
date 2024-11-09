package com.example.fj_2024_lesson_5.observer;

import com.example.fj_2024_lesson_5.entity.Location;
import com.example.fj_2024_lesson_5.repository.LocationRepository;

import java.util.List;

public class LocationDataObserver implements DataObserver<Location> {
    private final LocationRepository locationRepository;

    public LocationDataObserver(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public void update(List<Location> data) {
        locationRepository.saveAll(data);
    }
}
