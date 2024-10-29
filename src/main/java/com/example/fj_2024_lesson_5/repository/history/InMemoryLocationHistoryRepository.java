package com.example.fj_2024_lesson_5.repository.history;


import com.example.fj_2024_lesson_5.memento.LocationMemento;
import com.example.fj_2024_lesson_5.storage.HistoryStorage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryLocationHistoryRepository implements LocationHistoryRepository {
    private final HistoryStorage<String, List<LocationMemento>> historyStorage = new HistoryStorage<>();

    @Override
    public void save(String locationSlug, LocationMemento memento) {
        historyStorage.putIfAbsent(locationSlug, new ArrayList<>());
        historyStorage.get(locationSlug).add(memento);
    }

    @Override
    public List<LocationMemento> findByLocationSlug(String locationSlug) {
        return historyStorage.getOrDefault(locationSlug, new ArrayList<>());
    }
}