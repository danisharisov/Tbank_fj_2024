package com.example.fj_2024_lesson_5.storage;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HistoryStorage<ID, T> {

    private final Map<ID, T> storage = new ConcurrentHashMap<>();

    public void save(ID key, T value) {
        storage.put(key, value);
    }

    public T get(ID key) {
        return storage.get(key);
    }

    public Collection<T> findAll() {
        return storage.values();
    }

    public void update(ID key, T value) {
        storage.put(key, value);
    }

    public void delete(ID key) {
        storage.remove(key);
    }

    public T putIfAbsent(ID key, T value) {
        return storage.putIfAbsent(key, value);
    }

    public T getOrDefault(ID key, T defaultValue) {
        return storage.getOrDefault(key, defaultValue);
    }
}
