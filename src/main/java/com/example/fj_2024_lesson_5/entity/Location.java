package com.example.fj_2024_lesson_5.entity;

import com.example.fj_2024_lesson_5.memento.LocationMemento;
import com.example.fj_2024_lesson_5.memento.Memento;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Event> events;


    public void addEvent(Event event) {
        events.add(event);
        event.setLocation(this);
    }

    public void removeEvent(Event event) {
        events.remove(event);
        event.setLocation(null);
    }

    public LocationMemento makeSnapshot() {
        return new LocationMemento(id, name);
    }


    public void restore(Memento memento) {
        if (memento instanceof LocationMemento) {
            LocationMemento locationMemento = (LocationMemento) memento;
            this.id = locationMemento.getSlug();
            this.name = locationMemento.getName();
        } else {
            throw new IllegalArgumentException("Invalid memento type");
        }
    }

}

