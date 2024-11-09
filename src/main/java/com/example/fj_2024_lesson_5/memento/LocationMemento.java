package com.example.fj_2024_lesson_5.memento;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class LocationMemento implements  Memento, Serializable {

    private UUID slug;
    private String name;
    private LocalDateTime timestamp;

    public LocationMemento(UUID slug, String name) {
        this.slug = slug;
        this.name = name;
        timestamp = LocalDateTime.now();
    }

    @Override
    public LocalDateTime getSnapshotDate() {
        return timestamp;
    }

}
