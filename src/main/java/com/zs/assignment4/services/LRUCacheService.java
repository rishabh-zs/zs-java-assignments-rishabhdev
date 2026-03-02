package com.zs.assignment4.services;

import com.zs.assignment4.models.CacheEntry;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Lru cache service.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 */
public class LRUCacheService<K, V> {
    private final int capacity;
    private final Map<K, CacheEntry<K, V>> map;
    private final CacheEntry<K, V> head, tail;

    /**
     * Instantiates a new Lru cache service.
     *
     * @param capacity the capacity
     */
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

    /**
     * Get v.
     *
     * @param key the key
     * @return the v
     */
    public V get(K key) {
        CacheEntry<K, V> entry = map.get(key);
        if (entry == null) {
            return null;
        }
        removeNode(entry);
        addNodeToHead(entry);
        return entry.value;
    }

    /**
     * Put.
     *
     * @param key   the key
     * @param value the value
     */
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

    /**
     * Add to head.
     *
     * @param key   the key
     * @param value the value
     */
    public void addToHead(K key, V value) {
        put(key, value);
    }

    /**
     * Remove from tail cache entry.
     *
     * @return the cache entry
     */
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

    /**
     * Remove entry cache entry.
     *
     * @param key the key
     * @return the cache entry
     */
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

    /**
     * Gets cache display.
     *
     * @return the cache display
     */
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

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    private void removeNode(CacheEntry<K, V> entry) {
        if (entry == null || entry == head || entry == tail) {
            return;
        }
        entry.prev.next = entry.next;
        entry.next.prev = entry.prev;
    }

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

