package com.example.fj_2024_lesson_5.config;

import com.example.fj_2024_lesson_5.observer.CategoryDataObserver;
import com.example.fj_2024_lesson_5.observer.LocationDataObserver;
import com.example.fj_2024_lesson_5.repository.CategoryRepository;
import com.example.fj_2024_lesson_5.repository.LocationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObserverConfig {

    @Bean
    public CategoryDataObserver categoryDataObserver(CategoryRepository categoryRepository) {
        return new CategoryDataObserver(categoryRepository);
    }

    @Bean
    public LocationDataObserver locationDataObserver(LocationRepository locationRepository) {
        return new LocationDataObserver(locationRepository);
    }

}
