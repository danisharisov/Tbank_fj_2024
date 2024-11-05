package com.example.fj_2024_lesson_5.repository.history;


import com.example.fj_2024_lesson_5.memento.LocationMemento;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationHistoryRepository {
    void save(String locationSlug, LocationMemento memento);
    List<LocationMemento> findByLocationSlug(String slug);
    LocationMemento getLastSnapshot(String locationSlug);
    public void deleteLastSnapshot(String locationSlug);
    public void deleteHistory(String locationSlug);

}