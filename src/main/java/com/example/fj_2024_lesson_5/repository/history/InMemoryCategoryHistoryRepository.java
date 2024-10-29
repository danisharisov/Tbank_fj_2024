package com.example.fj_2024_lesson_5.repository.history;

import com.example.fj_2024_lesson_5.memento.CategoryMemento;
import com.example.fj_2024_lesson_5.storage.HistoryStorage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryCategoryHistoryRepository implements CategoryHistoryRepository {
    private final HistoryStorage<Long, List<CategoryMemento>> historyStorage = new HistoryStorage<>();

    @Override
    public void save(Long categoryId, CategoryMemento memento) {
        historyStorage.putIfAbsent(categoryId, new ArrayList<>());
        historyStorage.get(categoryId).add(memento);
    }

    @Override
    public List<CategoryMemento> findByCategoryId(Long categoryId) {
        return historyStorage.getOrDefault(categoryId, new ArrayList<>());
    }
}