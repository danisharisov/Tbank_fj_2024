package com.example.fj_2024_lesson_5.repository.history;


import com.example.fj_2024_lesson_5.memento.LocationMemento;
import com.example.fj_2024_lesson_5.storage.HistoryStorage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InMemoryLocationHistoryRepository implements LocationHistoryRepository {
    private final HistoryStorage<String, LocationMemento> historyStorage = new HistoryStorage<>();

    @Override
    public void save(String locationSlug, LocationMemento memento) {
        historyStorage.saveSnapshot(locationSlug, memento);
    }

    @Override
    public List<LocationMemento> findByLocationSlug(String locationSlug) {
        return historyStorage.getSnapshots(locationSlug);
    }

    public LocationMemento getLastSnapshot(String locationSlug) {
        return historyStorage.getLastSnapshot(locationSlug);
    }

    public void deleteLastSnapshot(String locationSlug) {
        historyStorage.deleteLastSnapshot(locationSlug);
    }

    public void deleteHistory(String locationSlug) {
        historyStorage.deleteHistory(locationSlug);
    }

}