package com.example.fj_2024_lesson_5.repository.history;

import com.example.fj_2024_lesson_5.memento.CategoryMemento;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryHistoryRepository {
    void save(Long categoryId, CategoryMemento memento);
    List<CategoryMemento> findByCategoryId(Long categoryId);
}