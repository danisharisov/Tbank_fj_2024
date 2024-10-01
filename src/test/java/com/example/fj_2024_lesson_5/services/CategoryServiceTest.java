package com.example.fj_2024_lesson_5.services;

import com.example.fj_2024_lesson_5.dto.Category;
import com.example.fj_2024_lesson_5.exceptions.CategoryNotFoundException;
import com.example.fj_2024_lesson_5.exceptions.EntityAlreadyExistsException;
import com.example.fj_2024_lesson_5.storage.CategoryStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryStorage categoryStorage;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories_PositiveScenario() {
        List<Category> mockCategories = Arrays.asList(new Category(1, "slug1", "Category1"), new Category(2, "slug2", "Category2"));
        when(categoryStorage.findAll()).thenReturn(mockCategories);

        List<Category> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        assertEquals("Category1", result.get(0).getName());
    }

    @Test
    void testGetCategoryById_PositiveScenario() {
        Category mockCategory = new Category(1, "slug1", "Category1");
        when(categoryStorage.findById(1)).thenReturn(mockCategory);

        Category result = categoryService.getCategoryById(1);

        assertNotNull(result);
        assertEquals("Category1", result.getName());
    }

    @Test
    void testGetCategoryById_NegativeScenario() {
        when(categoryStorage.findById(999)).thenReturn(null);

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(999));
    }

    @Test
    void testCreateCategory_Success() {
        Category newCategory = new Category(1, "slug1", "Category1");

        categoryService.createCategory(newCategory);

        verify(categoryStorage, times(1)).save(newCategory.getId(), newCategory);
    }

    @Test
    void testCreateCategory_EntityAlreadyExistsScenario() {
        Category existingCategory = new Category(1, "slug1", "Category1");
        when(categoryStorage.findById(1)).thenReturn(existingCategory);

        assertThrows(EntityAlreadyExistsException.class, () -> categoryService.createCategory(existingCategory));
    }

    @Test
    void testUpdateCategory_Success() {
        Category existingCategory = new Category(1, "slug1", "Category1");
        when(categoryStorage.findById(1)).thenReturn(existingCategory);

        Category updatedCategory = new Category(1, "slug1", "Updated Category");
        categoryService.updateCategory(updatedCategory);

        verify(categoryStorage, times(1)).update(1, updatedCategory);
    }

    @Test
    void testUpdateCategory_NotFound() {
        when(categoryStorage.findById(999)).thenReturn(null);
        Category updatedCategory = new Category(999, "slug999", "Updated Category");

        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(updatedCategory));
    }

    @Test
    void testDeleteCategory_Success() {
        Category existingCategory = new Category(1, "slug1", "Category1");
        when(categoryStorage.findById(1)).thenReturn(existingCategory);

        categoryService.deleteCategory(1);

        verify(categoryStorage, times(1)).delete(1);
    }

    @Test
    void testDeleteCategory_NotFound() {
        when(categoryStorage.findById(999)).thenReturn(null);

        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(999));
    }
}
