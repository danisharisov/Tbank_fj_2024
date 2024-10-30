package com.example.fj_2024_lesson_5.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class ExecutorConfig {

@Value("${threadpool.fixed.size}")
    private int fixedThreadPoolSize;

@Value("${threadpool.scheduled.size}")
    private int scheduledThreadPoolSize;

    @Bean
    public ExecutorService fixedThreadPool() {
        return Executors.newFixedThreadPool(fixedThreadPoolSize, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("FixedThreadPool-" + thread.getId());
            return thread;
        });
    }

    @Bean
    public ScheduledExecutorService scheduledThreadPool() {
        return Executors.newScheduledThreadPool(scheduledThreadPoolSize, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("ScheduledThreadPool-" + thread.getId());
            return thread;
        });
    }
}