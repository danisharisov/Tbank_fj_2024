package com.example.fj_2024_lesson_5.command;

import com.example.fj_2024_lesson_5.entity.Category;
import com.example.fj_2024_lesson_5.observer.DataObserver;
import com.example.fj_2024_lesson_5.services.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class InitializeCategoriesCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(InitializeCategoriesCommand.class);

    private final CategoryService categoryService;
    private final List<DataObserver<Category>> observers;

    public InitializeCategoriesCommand(CategoryService categoryService, List<DataObserver<Category>> observers) {
        this.categoryService = categoryService;
        this.observers = observers;
    }

    @Override
    public void execute() {
        try {
            logger.info("Fetching categories from KudaGo API...");
            List<Category> categories = categoryService.fetchCategoriesFromKudaGo();
            observers.forEach(observer -> observer.update(categories));
            logger.info("Categories initialized successfully.");
        } catch (Exception e) {
            logger.error("Failed to initialize categories: {}", e.getMessage());
        }
    }
}