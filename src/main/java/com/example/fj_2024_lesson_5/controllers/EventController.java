package com.example.fj_2024_lesson_5.controllers;

import com.example.fj_2024_lesson_5.entity.Event;
import com.example.fj_2024_lesson_5.exceptions.EventNotFoundException;
import com.example.fj_2024_lesson_5.repository.EventRepository;
import com.example.fj_2024_lesson_5.repository.LocationRepository;
import com.example.fj_2024_lesson_5.specification.EventSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;

    public EventController(EventRepository eventRepository, LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
    }

    // Создание события
    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        locationRepository.findById(event.getLocation().getId()).orElseThrow(() ->
                new IllegalArgumentException("Place with id " + event.getLocation().getId() + " not found")
        );
        Event savedEvent = eventRepository.save(event);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }

    // Получение события по ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable UUID id) {
        Event event = eventRepository.findById(id).orElseThrow(() ->
                new EventNotFoundException("Event not found with ID: " + id));
        return ResponseEntity.ok(event);
    }

    // Обновление события
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable UUID id, @Valid @RequestBody Event eventDetails) {
        Event event = eventRepository.findById(id).orElseThrow(() ->
                new EventNotFoundException("Event not found with ID: " + id));
        // Проверка существования места
        locationRepository.findById(eventDetails.getLocation().getId()).orElseThrow(() ->
                new IllegalArgumentException("Place with id " + eventDetails.getLocation().getId() + " not found")
        );

        event.setName(eventDetails.getName());
        event.setDate(eventDetails.getDate());
        event.setLocation(eventDetails.getLocation());

        Event updatedEvent = eventRepository.save(event);
        return ResponseEntity.ok(updatedEvent);
    }

    // Удаление события
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        Event event = eventRepository.findById(id).orElseThrow(() ->
                new EventNotFoundException("Event not found with ID: " + id));
        eventRepository.delete(event);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UUID locationId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate
    ) {
        Specification<Event> spec = Specification.where(EventSpecification.hasName(name))
                .and(EventSpecification.hasLocation(locationId))
                .and(EventSpecification.betweenDates(
                        fromDate != null ? LocalDate.parse(fromDate) : null,
                        toDate != null ? LocalDate.parse(toDate) : null));

        List<Event> events = eventRepository.findAll(spec);
        return ResponseEntity.ok(events);
    }
}