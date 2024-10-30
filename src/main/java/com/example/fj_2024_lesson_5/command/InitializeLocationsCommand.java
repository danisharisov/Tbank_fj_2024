package com.example.fj_2024_lesson_5.command;

import com.example.fj_2024_lesson_5.entity.Location;
import com.example.fj_2024_lesson_5.observer.DataObserver;
import com.example.fj_2024_lesson_5.services.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class InitializeLocationsCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(InitializeLocationsCommand.class);

    private final LocationService locationService;
    private final List<DataObserver<Location>> observers;

    public InitializeLocationsCommand(LocationService locationService, List<DataObserver<Location>> observers) {
        this.locationService = locationService;
        this.observers = observers;
    }

    @Override
    public void execute() {
        try {
            logger.info("Fetching locations from KudaGo API...");
            List<Location> locations = locationService.fetchLocationsFromKudaGo();
            observers.forEach(observer -> observer.update(locations));
            logger.info("Locations initialized successfully.");
        } catch (Exception e) {
            logger.error("Failed to initialize locations: {}", e.getMessage());
        }
    }
}
