package com.example.fj_2024_lesson_5.controllers;

import com.example.fj_2024_lesson_5.dto.Category;
import com.example.fj_2024_lesson_5.exceptions.CategoryNotFoundException;
import com.example.fj_2024_lesson_5.services.CategoryService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    void testGetAllCategories_Positive() throws Exception {
        List<Category> categories = Arrays.asList(
                new Category(1, "slug1", "Category1"),
                new Category(2, "slug2", "Category2")
        );
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/v1/places/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))  // Проверяем, что возвращается 2 объекта
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Category1"));
    }

    @Test
    void testGetCategoryById_Positive() throws Exception {
        Category category = new Category(1, "slug1", "Category1");
        when(categoryService.getCategoryById(1)).thenReturn(category);

        mockMvc.perform(get("/api/v1/places/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Category1"));
    }

    @Test
    void testGetCategoryById_NotFound() throws Exception {
        when(categoryService.getCategoryById(999)).thenThrow(new CategoryNotFoundException("Category not found"));

        mockMvc.perform(get("/api/v1/places/categories/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Category not found"));
    }
}
