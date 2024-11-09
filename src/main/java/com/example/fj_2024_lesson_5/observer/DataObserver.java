package com.example.fj_2024_lesson_5.observer;

import java.util.List;

public interface DataObserver<T> {
    void update(List<T> data);
}