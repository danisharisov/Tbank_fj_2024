package com.example.fj_2024_lesson_5.storage;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractStorage<ID, T> {
    protected final Map<ID, T> storage = new ConcurrentHashMap<>();

    public void save(ID key, T value) {
        storage.put(key, value);
    }

    public T findById(ID key) {
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
}