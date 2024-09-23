package com.example.fj_2024_lesson_5.storage;

import com.example.fj_2024_lesson_5.dto.Category;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class CategoryStorage {
    private final ConcurrentHashMap<Integer, Category> categories = new ConcurrentHashMap<>();

    public void save(Category category) {
        categories.put(category.getId(), category);
    }

    public List<Category> findAll() {
        return categories.values().stream().collect(Collectors.toList());
    }

    public Category findById(int id) {
        return categories.get(id);
    }

    public void update(Category category) {
        categories.put(category.getId(), category);
    }

    public void delete(int id) {
        categories.remove(id);
    }
}