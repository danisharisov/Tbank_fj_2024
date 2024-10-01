package com.example.fj_2024_lesson_5.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class Category {
    private int id;
    private String slug;
    private String name;
}