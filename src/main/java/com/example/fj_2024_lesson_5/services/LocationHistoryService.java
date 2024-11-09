package com.example.fj_2024_lesson_5.services;

import com.example.fj_2024_lesson_5.entity.Location;
import com.example.fj_2024_lesson_5.memento.LocationMemento;
import com.example.fj_2024_lesson_5.repository.history.LocationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LocationHistoryService {
    private final LocationHistoryRepository locationHistoryRepository;

    public void saveSnapshot(Location location) {
        LocationMemento snapshot = location.makeSnapshot();
        locationHistoryRepository.save(location.getId().toString(), snapshot);
    }

    public void restoreLocation(Location location) {
        LocationMemento lastSnapshot = locationHistoryRepository.getLastSnapshot(location.getId().toString());
        if (lastSnapshot != null) {
            location.restore(lastSnapshot);
            locationHistoryRepository.deleteLastSnapshot(location.getId().toString());
        }
    }
}