package com.example.fj_2024_lesson_5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Fj2024Lesson5Application {
    private static final Logger logger = LoggerFactory.getLogger(Fj2024Lesson5Application.class);
        public static void main(String[] args) {
            SpringApplication.run(Fj2024Lesson5Application.class, args);
            logger.info("Application started successfully.");
        }


    }

