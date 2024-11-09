package com.example.fj_2024_lesson_5.repository;

import com.example.fj_2024_lesson_5.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}