package com.example.fj_2024_lesson_5.services;

import com.example.fj_2024_lesson_5.dto.Category;
import com.example.fj_2024_lesson_5.storage.CategoryStorage;
import com.example.fj_2024_lesson_5.timed.Timed;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
@Timed
@Service
public class CategoryService {
    private final CategoryStorage categoryStorage;
    private final RestTemplate restTemplate;

    public CategoryService(CategoryStorage categoryStorage, RestTemplate restTemplate) {
        this.categoryStorage = categoryStorage;
        this.restTemplate = restTemplate;
    }

    public void fetchCategoriesFromKudaGo() {
        String url = "https://kudago.com/public-api/v1.4/place-categories";
        Category[] categories = restTemplate.getForObject(url, Category[].class);
        if (categories != null) {
            for (Category category : categories) {
                categoryStorage.save(category);
            }
        }
    }

    public List<Category> getAllCategories() {
        return categoryStorage.findAll();
    }

    public Category getCategoryById(int id) {
        return categoryStorage.findById(id);
    }

    public void createCategory(Category category) {
        categoryStorage.save(category);
    }

    public void updateCategory(Category category) {
        categoryStorage.update(category);
    }

    public void deleteCategory(int id) {
        categoryStorage.delete(id);
    }
}