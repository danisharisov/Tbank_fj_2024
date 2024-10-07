package com.example.fj_2024_lesson_5.init;

import com.example.fj_2024_lesson_5.services.CategoryService;
import com.example.fj_2024_lesson_5.services.LocationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class DataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final CategoryService categoryService;
    private final LocationService locationService;

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent() {
        try {
            logger.info("Fetching categories from KudaGo API...");
            categoryService.fetchCategoriesFromKudaGo();
            logger.info("Categories initialized successfully.");
        } catch (Exception e) {
            logger.error("Failed to initialize categories: {}", e.getMessage());
        }

        try {
            logger.info("Fetching locations from KudaGo API...");
            locationService.fetchLocationsFromKudaGo();
            logger.info("Locations initialized successfully.");
        } catch (Exception e) {
            logger.error("Failed to initialize locations: {}", e.getMessage());
        }
    }
}
