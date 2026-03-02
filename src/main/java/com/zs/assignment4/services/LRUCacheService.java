package com.zs.assignment4.services;

import com.zs.assignment4.models.CacheEntry;
import java.util.HashMap;
import java.util.Map;

public class LRUCacheService<K, V> {
    private final int capacity;
    private final Map<K, CacheEntry<K, V>> map;
    private final CacheEntry<K, V> head, tail;

    public LRUCacheService(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0.");
        }
        this.capacity = capacity;
        this.map = new HashMap<>();
        head = new CacheEntry<>(null, null);
        tail = new CacheEntry<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    // Get value by key, if key exists move entry to head (most recently used)
    public V get(K key) {
        CacheEntry<K, V> entry = map.get(key);
        if (entry == null) {
            return null;
        }
        removeNode(entry);
        addNodeToHead(entry);
        return entry.value;
    }

    //put key-value pair in cache, if key exists update value and move to head, if cache is full remove LRU entry
    public void put(K key, V value) {
        validateKeyAndValue(key, value);
        CacheEntry<K, V> existing = map.get(key);
        if (existing != null) {
            existing.value = value;
            removeNode(existing);
            addNodeToHead(existing);
            return;
        }

        if (map.size() >= capacity) {
            removeFromTail();
        }

        CacheEntry<K, V> newEntry = new CacheEntry<>(key, value);
        addNodeToHead(newEntry);
        map.put(key, newEntry);
    }

    // Add or move a key-value entry to MRU position
    public void addToHead(K key, V value) {
        put(key, value);
    }

    // Remove and return LRU entry
    public CacheEntry<K, V> removeFromTail() {
        if (isEmpty()) {
            return null;
        }
        CacheEntry<K, V> lru = tail.prev;
        removeNode(lru);
        map.remove(lru.key);
        lru.prev = null;
        lru.next = null;
        return lru;
    }

    // Remove a specific entry by key
    public CacheEntry<K, V> removeEntry(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }

        CacheEntry<K, V> entry = map.get(key);
        if (entry == null) {
            return null;
        }

        removeNode(entry);
        map.remove(key);
        entry.prev = null;
        entry.next = null;
        return entry;
    }

    public String getCacheDisplay() {
        if (isEmpty()) {
            return "Cache is empty.";
        }
        StringBuilder sb = new StringBuilder();
        CacheEntry<K, V> current = head.next;
        while (current != tail) {
            sb.append(current.key).append("=").append(current.value);
            if (current.next != tail) {
                sb.append(" -> ");
            }
            current = current.next;
        }
        return sb.toString();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    // Remove entry from the list
    private void removeNode(CacheEntry<K, V> entry) {
        if (entry == null || entry == head || entry == tail) {
            return;
        }
        entry.prev.next = entry.next;
        entry.next.prev = entry.prev;
    }

    // Add entry to the head of the list (most recently used)
    private void addNodeToHead(CacheEntry<K, V> entry) {
        entry.next = head.next;
        entry.next.prev = entry;
        head.next = entry;
        entry.prev = head;
    }

    private void validateKeyAndValue(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null.");
        }
    }
}

