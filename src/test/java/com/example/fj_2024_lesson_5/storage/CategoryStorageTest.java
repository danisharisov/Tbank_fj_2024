package com.example.fj_2024_lesson_5.storage;

import com.example.fj_2024_lesson_5.dto.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryStorageTest {

    private CategoryStorage categoryStorage;

    @BeforeEach
    void setUp() {
        categoryStorage = new CategoryStorage();
    }

    @Test
    void testSaveCategory() {
        Category category = new Category(1, "slug1", "Category1");
        categoryStorage.save(1, category);  // Сохраняем категорию

        assertEquals(category, categoryStorage.findById(1));
    }

    @Test
    void testFindById_Found() {
        Category category = new Category(1, "slug1", "Category1");
        categoryStorage.save(1, category);

        Category result = categoryStorage.findById(1);
        assertNotNull(result);
        assertEquals("Category1", result.getName());
    }

    @Test
    void testFindById_NotFound() {
        assertNull(categoryStorage.findById(999));
    }

    @Test
    void testFindAllCategories() {
        Category category1 = new Category(1, "slug1", "Category1");
        Category category2 = new Category(2, "slug2", "Category2");
        categoryStorage.save(1, category1);
        categoryStorage.save(2, category2);

        Collection<Category> categories = categoryStorage.findAll();

        List<Category> categoryList = categories.stream().toList();

        assertEquals(2, categoryList.size());
        assertTrue(categoryList.contains(category1));
        assertTrue(categoryList.contains(category2));
    }

    @Test
    void testDeleteCategory() {
        Category category = new Category(1, "slug1", "Category1");
        categoryStorage.save(1, category);

        categoryStorage.delete(1);
        assertNull(categoryStorage.findById(1));
    }
}
