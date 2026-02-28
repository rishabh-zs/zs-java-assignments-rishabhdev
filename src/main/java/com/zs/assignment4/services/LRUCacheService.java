package com.zs.assignment4.services;

import com.zs.assignment4.models.CacheEntry;
import java.util.HashMap;
import java.util.Map;

public class LRUCacheService<K, V> {
    private final int capacity;
    private final Map<K, CacheEntry<K, V>> map;
    private final CacheEntry<K, V> head, tail;

    // Initializes the LRU Cache with a specified capacity
    public LRUCacheService(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        head = new CacheEntry<>(null, null);
        tail = new CacheEntry<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    // Retrieves a value from the cache and updates its position to the most recently used
    public V get(K key) {
        if (!map.containsKey(key)) return null;
        CacheEntry<K, V> entry = map.get(key);
        remove(entry);
        addToHead(entry);
        return entry.value;
    }

    // Adds or updates an entry in the cache
    public void put(K key, V value) {
        if (map.containsKey(key)) {
            remove(map.get(key));
        }
        if (map.size() == capacity) {
            map.remove(tail.prev.key);
            remove(tail.prev);
        }
        CacheEntry<K, V> newEntry = new CacheEntry<>(key, value);
        addToHead(newEntry);
        map.put(key, newEntry);
    }

    // Removes an entry from the linked list
    private void remove(CacheEntry<K, V> entry) {
        entry.prev.next = entry.next;
        entry.next.prev = entry.prev;
    }

    // Adds an entry to the head of the linked list (most recently used position)
    private void addToHead(CacheEntry<K, V> entry) {
        entry.next = head.next;
        entry.next.prev = entry;
        head.next = entry;
        entry.prev = head;
    }
}