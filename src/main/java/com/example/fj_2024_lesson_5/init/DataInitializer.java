package com.example.fj_2024_lesson_5.init;

import com.example.fj_2024_lesson_5.command.Command;
import com.example.fj_2024_lesson_5.command.InitializeCategoriesCommand;
import com.example.fj_2024_lesson_5.command.InitializeLocationsCommand;
import com.example.fj_2024_lesson_5.observer.CategoryDataObserver;
import com.example.fj_2024_lesson_5.observer.LocationDataObserver;
import com.example.fj_2024_lesson_5.services.CategoryService;
import com.example.fj_2024_lesson_5.services.LocationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final CategoryService categoryService;
    private final LocationService locationService;
    private final CategoryDataObserver categoryDataObserver;
    private final LocationDataObserver locationDataObserver;
    private final ExecutorService fixedThreadPool;
    private final ScheduledExecutorService scheduledThreadPool;

    @EventListener(ApplicationStartedEvent.class)
    public void onApplicationEvent() {
        logger.info("Scheduling data initialization...");
        scheduledThreadPool.scheduleAtFixedRate(this::initializeData, 0, 1, TimeUnit.HOURS);
    }

    private void initializeData() {
        long startTime = System.nanoTime();

        Command initializeCategories = new InitializeCategoriesCommand(categoryService, Arrays.asList(categoryDataObserver));
        Command initializeLocations = new InitializeLocationsCommand(locationService, Arrays.asList(locationDataObserver));

        List<Future<?>> futures = Arrays.asList(
                fixedThreadPool.submit(initializeCategories::execute),
                fixedThreadPool.submit(initializeLocations::execute)
        );

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