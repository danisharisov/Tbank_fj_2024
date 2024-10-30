package com.example.fj_2024_lesson_5.init;

import com.example.fj_2024_lesson_5.services.CategoryService;
import com.example.fj_2024_lesson_5.services.LocationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final CategoryService categoryService;
    private final LocationService locationService;
    private final ExecutorService fixedThreadPool;
    private final ScheduledExecutorService scheduledThreadPool;

    @EventListener(ApplicationStartedEvent.class)
    public void onApplicationEvent() {
        logger.info("Scheduling data initialization...");
        scheduledThreadPool.scheduleAtFixedRate(this::initializeData, 0, 1, TimeUnit.HOURS);
    }

    private void initializeData() {
        long startTime = System.nanoTime();

        List<Future<?>> futures = new ArrayList<>();

        Future<?> categoriesFuture = fixedThreadPool.submit(() -> {
            try {
                logger.info("Fetching categories from KudaGo API...");
                categoryService.fetchCategoriesFromKudaGo();
                logger.info("Categories initialized successfully.");
            } catch (Exception e) {
                logger.error("Failed to initialize categories: {}", e.getMessage());
            }
        });
        futures.add(categoriesFuture);

        Future<?> locationsFuture = fixedThreadPool.submit(() -> {
            try {
                logger.info("Fetching locations from KudaGo API...");
                locationService.fetchLocationsFromKudaGo();
                logger.info("Locations initialized successfully.");
            } catch (Exception e) {
                logger.error("Failed to initialize locations: {}", e.getMessage());
            }
        });
        futures.add(locationsFuture);

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                logger.error("Task execution failed: {}", e.getMessage());
            }
        }

        long endTime = System.nanoTime();
        long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        logger.info("Initialization took {} ms", duration);
    }
}