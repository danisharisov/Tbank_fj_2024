package com.example.fj_2024_lesson_5.observer;


import com.example.fj_2024_lesson_5.entity.Category;
import com.example.fj_2024_lesson_5.repository.CategoryRepository;

import java.util.List;

public class CategoryDataObserver implements DataObserver<Category> {
    private final CategoryRepository categoryRepository;

    public CategoryDataObserver(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void update(List<Category> data) {
        categoryRepository.saveAll(data);
    }
}
