package com.example.fj_2024_lesson_5.services;

import com.example.fj_2024_lesson_5.entity.Event;
import com.example.fj_2024_lesson_5.entity.Location;
import com.example.fj_2024_lesson_5.exceptions.EventNotFoundException;
import com.example.fj_2024_lesson_5.repository.EventRepository;
import com.example.fj_2024_lesson_5.repository.LocationRepository;
import com.example.fj_2024_lesson_5.specification.EventSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;

    public EventService(EventRepository eventRepository, LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
    }

    public Event createEvent(Event event) {
        Location location = locationRepository.findById(event.getLocation().getId()).orElseThrow(() ->
                new IllegalArgumentException("Place with id " + event.getLocation().getId() + " not found"));
        event.setLocation(location);
        return eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public Event getEventById(UUID id) {
        return eventRepository.findById(id).orElseThrow(() ->
                new EventNotFoundException("Event not found with ID: " + id));
    }

    public Event updateEvent(UUID id, Event eventDetails) {
        Event event = eventRepository.findById(id).orElseThrow(() ->
                new EventNotFoundException("Event not found with ID: " + id));

        Location location = locationRepository.findById(eventDetails.getLocation().getId()).orElseThrow(() ->
                new IllegalArgumentException("Place with id " + eventDetails.getLocation().getId() + " not found"));

        event.setName(eventDetails.getName());
        event.setDate(eventDetails.getDate());
        event.setLocation(location);

        return eventRepository.save(event);
    }

    public void deleteEvent(UUID id) {
        Event event = eventRepository.findById(id).orElseThrow(() ->
                new EventNotFoundException("Event not found with ID: " + id));
        eventRepository.delete(event);
    }

    @Transactional(readOnly = true)
    public List<Event> searchEvents(String name, UUID locationId, String fromDate, String toDate) {
        Specification<Event> spec = Specification.where(EventSpecification.hasName(name))
                .and(EventSpecification.hasLocation(locationId))
                .and(EventSpecification.betweenDates(
                        fromDate != null ? LocalDate.parse(fromDate) : null,
                        toDate != null ? LocalDate.parse(toDate) : null));
        return eventRepository.findAll(spec);
    }
}
