package com.example.fj_2024_lesson_5.services;

import com.example.fj_2024_lesson_5.client.KudaGoClient;
import com.example.fj_2024_lesson_5.entity.Category;
import com.example.fj_2024_lesson_5.exceptions.CategoryNotFoundException;
import com.example.fj_2024_lesson_5.memento.CategoryMemento;
import com.example.fj_2024_lesson_5.repository.CategoryRepository;
import com.example.fj_2024_lesson_5.repository.history.CategoryHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final KudaGoClient kudaGoClient;
    private final CategoryHistoryRepository categoryHistoryRepository;

    public Category createCategory(String name) {
        Category category = new Category(name);
        saveCategoryMemento(category);
        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));
    }

    public Category updateCategory(Long id, String newName) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));
        category.setName(newName);
        saveCategoryMemento(category);
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));
        saveCategoryMemento(category);
        categoryRepository.delete(category);
    }

    public List<Category> fetchCategoriesFromKudaGo() {
        List<Category> categories = kudaGoClient.getAllCategories();
        categoryRepository.saveAll(categories);
        return categories;
    }

    private void saveCategoryMemento(Category category) {
        CategoryMemento memento = new CategoryMemento(category.getId(), category.getName(),category.getSlug());
        categoryHistoryRepository.save(category.getId(), memento);
    }
}
