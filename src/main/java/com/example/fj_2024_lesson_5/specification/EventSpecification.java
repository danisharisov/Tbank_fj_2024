package com.example.fj_2024_lesson_5.specification;

import com.example.fj_2024_lesson_5.entity.Event;
import com.example.fj_2024_lesson_5.entity.Location;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class EventSpecification {

    public static Specification<Event> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Event> hasLocation(UUID locationId) {
        return (root, query, criteriaBuilder) -> {
            if (locationId == null) {
                return null;
            }
            Join<Event, Location> locationJoin = root.join("location", JoinType.INNER);
            return criteriaBuilder.equal(locationJoin.get("id"), locationId);
        };
    }

    public static Specification<Event> betweenDates(LocalDate fromDate, LocalDate toDate) {
        return (root, query, criteriaBuilder) -> {
            if (fromDate != null && toDate != null) {
                return criteriaBuilder.between(root.get("date"), fromDate, toDate);
            } else if (fromDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("date"), fromDate);
            } else if (toDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("date"), toDate);
            }
            return null;
        };
    }
}