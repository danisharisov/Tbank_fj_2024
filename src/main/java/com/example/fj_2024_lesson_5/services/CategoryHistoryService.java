package com.example.fj_2024_lesson_5.services;

import com.example.fj_2024_lesson_5.entity.Category;
import com.example.fj_2024_lesson_5.memento.CategoryMemento;
import com.example.fj_2024_lesson_5.memento.Memento;
import com.example.fj_2024_lesson_5.repository.history.CategoryHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CategoryHistoryService {
    private final CategoryHistoryRepository categoryHistoryRepository;

    public void saveSnapshot(Category category) {
        Memento snapshot = category.makeSnapshot();
        categoryHistoryRepository.save(category.getId(), (CategoryMemento) snapshot);
    }

    public void restoreCategory(Category category) {
        CategoryMemento lastSnapshot = categoryHistoryRepository.getLastSnapshot(category.getId());
        if (lastSnapshot != null) {
            category.restore(lastSnapshot);
            categoryHistoryRepository.deleteLastSnapshot(category.getId());
        }
    }
}
