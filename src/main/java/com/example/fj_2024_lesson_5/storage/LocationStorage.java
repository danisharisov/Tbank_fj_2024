package com.example.fj_2024_lesson_5.storage;

import com.example.fj_2024_lesson_5.dto.Location;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class LocationStorage {
    private final ConcurrentHashMap<String, Location> locations = new ConcurrentHashMap<>();

    public void save(Location location) {
        locations.put(location.getSlug(), location);
    }

    public void saveAll(Collection<Location> locations) {
        for (Location location : locations) {
            save(location);
        }
    }

    public Collection<Location> findAll() {
        return locations.values();
    }

    public Location findBySlug(String slug) {
        return locations.get(slug);
    }

    public void deleteBySlug(String slug) {
        locations.remove(slug);
    }
}