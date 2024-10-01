package com.example.fj_2024_lesson_5.controllers;

import com.example.fj_2024_lesson_5.dto.Category;
import com.example.fj_2024_lesson_5.exceptions.CategoryNotFoundException;
import com.example.fj_2024_lesson_5.exceptions.EntityAlreadyExistsException;
import com.example.fj_2024_lesson_5.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private Category testCategory1;
    private Category testCategory2;

    private static final String BASE_URL = "/api/v1/places/categories";

    @BeforeEach
    public void setup() {
        testCategory1 = new Category(1, "museums", "Museums");
        testCategory2 = new Category(2, "parks", "Parks");
    }

    @Test
    public void testGetAllCategories() throws Exception {
        List<Category> mockCategories = Arrays.asList(testCategory1, testCategory2);
        Mockito.when(categoryService.getAllCategories()).thenReturn(mockCategories);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Museums"))
                .andExpect(jsonPath("$[1].name").value("Parks"));
    }

    @Test
    public void testGetCategoryById_Success() throws Exception {
        Mockito.when(categoryService.getCategoryById(1)).thenReturn(testCategory1);

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Museums"))
                .andExpect(jsonPath("$.slug").value("museums"));
    }

    @Test
    public void testGetCategoryById_NotFound() throws Exception {
        Mockito.when(categoryService.getCategoryById(99)).thenThrow(new CategoryNotFoundException("Category not found"));

        mockMvc.perform(get(BASE_URL + "/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateCategory_Success() throws Exception {
        Mockito.doNothing().when(categoryService).createCategory(any(Category.class));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategory1)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateCategory_Conflict() throws Exception {
        Mockito.doThrow(new EntityAlreadyExistsException("Category already exists")).when(categoryService).createCategory(any(Category.class));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategory1)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testUpdateCategory_Success() throws Exception {
        Mockito.doNothing().when(categoryService).updateCategory(any(Category.class));

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategory1)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateCategory_NotFound() throws Exception {
        Mockito.doThrow(new CategoryNotFoundException("Category not found")).when(categoryService).updateCategory(any(Category.class));

        mockMvc.perform(put(BASE_URL + "/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategory1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteCategory_Success() throws Exception {
        Mockito.doNothing().when(categoryService).deleteCategory(1);

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteCategory_NotFound() throws Exception {
        Mockito.doThrow(new CategoryNotFoundException("Category not found")).when(categoryService).deleteCategory(99);

        mockMvc.perform(delete(BASE_URL + "/99"))
                .andExpect(status().isNotFound());
    }
}
