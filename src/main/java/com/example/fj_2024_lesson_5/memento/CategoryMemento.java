package com.example.fj_2024_lesson_5.memento;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CategoryMemento implements Memento, Serializable {

    private Long id;
    private String name;
    private String slug;
    private LocalDateTime timestamp;

    public CategoryMemento(Long id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        timestamp = LocalDateTime.now();
    }

    @Override
    public LocalDateTime getSnapshotDate() {
        return timestamp;
    }


}