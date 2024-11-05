package com.example.fj_2024_lesson_5.services;

import com.example.fj_2024_lesson_5.client.KudaGoClient;
import com.example.fj_2024_lesson_5.entity.Category;
import com.example.fj_2024_lesson_5.exceptions.CategoryNotFoundException;
import com.example.fj_2024_lesson_5.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final KudaGoClient kudaGoClient;
    private final CategoryHistoryService categoryHistoryService;

    public Category createCategory(String name) {
        Category category = new Category(name);
        categoryHistoryService.saveSnapshot(category);
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

    public Category updateCategory(Long id, String newName, String newSlug) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));
        categoryHistoryService.saveSnapshot(category);
        category.setName(newName);
        category.setSlug(newSlug);
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));
        categoryHistoryService.saveSnapshot(category);
        category.setName(null);
        category.setSlug(null);
        categoryRepository.save(category);
    }

    public List<Category> fetchCategoriesFromKudaGo() {
        List<Category> categories = kudaGoClient.getAllCategories();
        categoryRepository.saveAll(categories);
        return categories;
    }

    public void restoreCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));
        categoryHistoryService.restoreCategory(category);
        categoryRepository.save(category);
    }
}
