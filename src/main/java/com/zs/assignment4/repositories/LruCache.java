package com.zs.assignment4.repositories;

import com.zs.assignment4.models.CacheEntry;
import com.zs.assignment4.models.Category;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Lru cache.
 */
public class LruCache {
    private final int capacity;
    private final Map<String, CacheEntry<String, Category>> cache;

    private CacheEntry<String, Category> head;
    private CacheEntry<String, Category> tail;

    /**
     * Instantiates a new Lru cache.
     *
     * @param capacity the capacity
     */
    public LruCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
    }

    private void removeNode(CacheEntry<String, Category> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }

    private void addToHead(CacheEntry<String, Category> node) {
        node.next = head;
        node.prev = null;
        if (head != null) {
            head.prev = node;
        }
        head = node;
        if (tail == null) {
            tail = head;
        }
    }

    private void moveToHead(CacheEntry<String, Category> node) {
        removeNode(node);
        addToHead(node);
    }

    /**
     * Get category.
     *
     * @param key the key
     * @return the category
     */
    public Category get(String key) {
        String lowerKey = key.toLowerCase();
        if (cache.containsKey(lowerKey)) {
            CacheEntry<String, Category> node = cache.get(lowerKey);
            moveToHead(node);
            return node.value;
        }
        return null;
    }

    /**
     * Put.
     *
     * @param key      the key
     * @param category the category
     */
    public void put(String key, Category category) {
        String lowerKey = key.toLowerCase();

        if (cache.containsKey(lowerKey)) {
            CacheEntry<String, Category> node = cache.get(lowerKey);
            node.value = category;
            moveToHead(node);
        } else {
            CacheEntry<String, Category> newNode = new CacheEntry<>(lowerKey, category);
            cache.put(lowerKey, newNode);
            addToHead(newNode);

            if (cache.size() > capacity) {
                cache.remove(tail.key);
                removeNode(tail);
            }
        }
    }

    /**
     * Remove.
     *
     * @param key the key
     */
    public void remove(String key) {
        String lowerKey = key.toLowerCase();
        if (cache.containsKey(lowerKey)) {
            CacheEntry<String, Category> node = cache.get(lowerKey);
            removeNode(node);
            cache.remove(lowerKey);
        }
    }

    /**
     * Gets cached categories.
     *
     * @return the cached categories
     */
    public List<Category> getCachedCategories() {
        List<Category> categories = new ArrayList<>();
        CacheEntry<String, Category> current = head;
        while (current != null) {
            categories.add(current.value);
            current = current.next;
        }
        return categories;
    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty() {
        return head == null;
    }
}
