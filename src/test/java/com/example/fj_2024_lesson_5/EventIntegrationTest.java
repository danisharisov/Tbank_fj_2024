package com.example.fj_2024_lesson_5;

import com.example.fj_2024_lesson_5.entity.Event;
import com.example.fj_2024_lesson_5.entity.Location;
import com.example.fj_2024_lesson_5.repository.EventRepository;
import com.example.fj_2024_lesson_5.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.*;


@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = { PostgreSQLInitializer.class })
public class EventIntegrationTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Test
    void testCreateEvent() {
        Location location = new Location();
        location.setName("London");
        Location savedLocation = locationRepository.save(location);

        Event event = new Event();
        event.setName("Concert");
        event.setDate(LocalDate.of(2024, 10, 21));
        event.setLocation(savedLocation);

        Event savedEvent = eventRepository.save(event);

        assertNotNull(savedEvent.getId());
        assertEquals("Concert", savedEvent.getName());
        assertEquals(savedLocation, savedEvent.getLocation());
    }

    @Test
    void testFindEventById() {
        Location location = new Location();
        location.setName("Berlin");
        Location savedLocation = locationRepository.save(location);

        Event event = new Event();
        event.setName("Conference");
        event.setDate(LocalDate.of(2024, 11, 15));
        event.setLocation(savedLocation);

        Event savedEvent = eventRepository.save(event);

        Optional<Event> foundEvent = eventRepository.findById(savedEvent.getId());
        assertTrue(foundEvent.isPresent());
        assertEquals("Conference", foundEvent.get().getName());
    }

    @Test
    void testDeleteEvent() {
        Location location = new Location();
        location.setName("Rome");
        Location savedLocation = locationRepository.save(location);

        Event event = new Event();
        event.setName("Festival");
        event.setDate(LocalDate.of(2024, 12, 30));
        event.setLocation(savedLocation);

        Event savedEvent = eventRepository.save(event);
        eventRepository.deleteById(savedEvent.getId());

        Optional<Event> deletedEvent = eventRepository.findById(savedEvent.getId());
        assertFalse(deletedEvent.isPresent());
    }

    @BeforeEach
    public void cleanUp() {
        eventRepository.deleteAll();
        locationRepository.deleteAll();
    }

}
