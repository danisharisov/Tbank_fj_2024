package com.example.fj_2024_lesson_5.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HistoryStorage<ID, T> {

    private final Map<ID, List<T>> storage = new ConcurrentHashMap<>();

    public void saveSnapshot(ID key, T value) {
        storage.putIfAbsent(key, new ArrayList<>());
        storage.get(key).add(value);
    }

    public List<T> getSnapshots(ID key) {
        return storage.getOrDefault(key, new ArrayList<>());
    }

    public T getLastSnapshot(ID key) {
        List<T> history = storage.get(key);
        if (history != null && !history.isEmpty()) {
            return history.get(history.size() - 1);
        }
        return null;
    }

    public void deleteHistory(ID key) {
        storage.remove(key);
    }

    public void deleteLastSnapshot(ID key) {
        List<T> history = storage.get(key);
        if (history != null && !history.isEmpty()) {
            history.remove(history.size() - 1);
        }
    }
}
