package com.example.fj_2024_lesson_5.repository.history;

import com.example.fj_2024_lesson_5.memento.CategoryMemento;
import com.example.fj_2024_lesson_5.storage.HistoryStorage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InMemoryCategoryHistoryRepository implements CategoryHistoryRepository {
    private final HistoryStorage<Long, CategoryMemento> historyStorage = new HistoryStorage<>();

    @Override
    public void save(Long categoryId, CategoryMemento memento) {
        historyStorage.saveSnapshot(categoryId, memento);
    }

    @Override
    public List<CategoryMemento> findByCategoryId(Long categoryId) {
        return historyStorage.getSnapshots(categoryId);
    }

    public CategoryMemento getLastSnapshot(Long categoryId) {
        return historyStorage.getLastSnapshot(categoryId);
    }

    public void deleteLastSnapshot(Long categoryId) {
        historyStorage.deleteLastSnapshot(categoryId);
    }

    public void deleteHistory(Long categoryId) {
        historyStorage.deleteHistory(categoryId);
    }

}