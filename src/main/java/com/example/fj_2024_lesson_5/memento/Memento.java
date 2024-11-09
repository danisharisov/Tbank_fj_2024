package com.example.fj_2024_lesson_5.memento;

import java.time.LocalDateTime;

public interface Memento {
    String getName();
    LocalDateTime getSnapshotDate();
}