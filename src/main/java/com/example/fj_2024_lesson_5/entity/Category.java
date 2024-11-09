package com.example.fj_2024_lesson_5.entity;

import com.example.fj_2024_lesson_5.memento.CategoryMemento;
import com.example.fj_2024_lesson_5.memento.Memento;
import com.example.fj_2024_lesson_5.repository.history.CategoryHistoryRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String slug;

    public Category(String name) {
        this.name = name;
    }

    @Autowired
    private transient CategoryHistoryRepository categoryHistoryRepository;

    public Memento makeSnapshot() {
        return new CategoryMemento(id, name, slug);
    }

    public void restore(Memento memento) {
        if (memento instanceof CategoryMemento) {
            CategoryMemento categoryMemento = (CategoryMemento) memento;
            this.id = categoryMemento.getId();
            this.name = categoryMemento.getName();
            this.slug = categoryMemento.getSlug();
        } else {
            throw new IllegalArgumentException("Invalid memento type");
        }
    }
}

