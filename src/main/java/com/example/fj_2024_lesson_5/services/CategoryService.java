package com.example.fj_2024_lesson_5.services;

import com.example.fj_2024_lesson_5.dto.Category;
import com.example.fj_2024_lesson_5.exceptions.CategoryNotFoundException;
import com.example.fj_2024_lesson_5.exceptions.EntityAlreadyExistsException;
import com.example.fj_2024_lesson_5.storage.CategoryStorage;
import com.example.fj_2024_lesson_5.timed.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
@Timed
@Service
@RequiredArgsConstructor
public class CategoryService {
    @Value("${kudago.category.api.url}")
    private String categoryUrl;
    private final CategoryStorage categoryStorage;
    private final RestTemplate restTemplate;

    public void fetchCategoriesFromKudaGo() {
        Category[] categories = restTemplate.getForObject(categoryUrl, Category[].class);
        if (categories != null) {
            saveAll(List.of(categories));
        }
    }

    public List<Category> getAllCategories() {
        return List.copyOf(categoryStorage.findAll());
    }

    public Category getCategoryById(int id) {
        Category category = categoryStorage.findById(id);
        if (category == null) {
            throw new CategoryNotFoundException("Category with ID " + id + " not found");
        }
        return category;
    }

    public void createCategory(Category category) {
        if (categoryStorage.findById(category.getId()) != null) {
            throw new EntityAlreadyExistsException("Category with ID " + category.getId() + " already exists.");
        }
        categoryStorage.save(category.getId(), category);
    }

    public void updateCategory(Category category) {
        if (categoryStorage.findById(category.getId()) == null) {
            throw new CategoryNotFoundException("Category with ID " + category.getId() + " not found");
        }
        categoryStorage.update(category.getId(), category);
    }

    public void saveAll(List<Category> categories) {
        categories.forEach(category -> categoryStorage.save(category.getId(), category));
    }

    public void deleteCategory(int id) {
        if (categoryStorage.findById(id) == null) {
            throw new CategoryNotFoundException("Category with ID " + id + " not found");
        }
        categoryStorage.delete(id);
    }
}