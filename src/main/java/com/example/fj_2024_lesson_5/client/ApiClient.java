package com.example.fj_2024_lesson_5.client;

import java.util.List;

public interface ApiClient <E> {
    List<E> getAllEntitiesFromKudaGo();
}
