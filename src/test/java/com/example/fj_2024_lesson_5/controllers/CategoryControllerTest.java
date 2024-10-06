package com.example.fj_2024_lesson_5.controllers;

import com.example.fj_2024_lesson_5.dto.Category;
import com.example.fj_2024_lesson_5.exceptions.CategoryNotFoundException;
import com.example.fj_2024_lesson_5.exceptions.EntityAlreadyExistsException;
import com.example.fj_2024_lesson_5.services.CategoryService;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    void testGetAllCategories_Positive() throws Exception {
        List<Category> categories = List.of(new Category(1, "slug1", "Category1"), new Category(2, "slug2", "Category2"));
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/v1/places/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void testGetAllCategories_EmptyList() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/places/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
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
                .andExpect(content().string(containsString("Category not found")));
    }

    @Test
    void testCreateCategory_Success() throws Exception {
        Category category = new Category(1, "slug1", "Category1");

        mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1, \"slug\":\"slug1\", \"name\":\"Category1\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateCategory_EntityAlreadyExists() throws Exception {
        Category category = new Category(1, "slug1", "Category1");
        Mockito.doThrow(new EntityAlreadyExistsException("Category already exists")).when(categoryService).createCategory(Mockito.any(Category.class));

        mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1, \"slug\":\"slug1\", \"name\":\"Category1\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void testUpdateCategory_Success() throws Exception {
        Category category = new Category(1, "slug1", "Updated Category");

        mockMvc.perform(put("/api/v1/places/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1, \"slug\":\"slug1\", \"name\":\"Updated Category\"}"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateCategory_NotFound() throws Exception {
        Category category = new Category(1, "slug1", "Updated Category");
        Mockito.doThrow(new CategoryNotFoundException("Category not found")).when(categoryService).updateCategory(Mockito.any(Category.class));

        mockMvc.perform(put("/api/v1/places/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1, \"slug\":\"slug1\", \"name\":\"Updated Category\"}"))
                .andExpect(status().isNotFound()); // Ожидается 404 Not Found
    }
    @Test
    void testDeleteCategory_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/places/categories/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteCategory_NotFound() throws Exception {
        Mockito.doThrow(new CategoryNotFoundException("Category not found")).when(categoryService).deleteCategory(999);

        mockMvc.perform(delete("/api/v1/places/categories/999"))
                .andExpect(status().isNotFound());
    }


}