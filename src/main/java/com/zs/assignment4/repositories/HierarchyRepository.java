package com.zs.assignment4.repositories;

import com.zs.assignment4.models.CacheEntry;
import com.zs.assignment4.models.Category;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Hierarchy repository.
 */
public class HierarchyRepository {
    private final int capacity;
    private final Map<String, CacheEntry> cache;

    private CacheEntry head;
    private CacheEntry tail;

    /**
     * Instantiates a new Hierarchy repository.
     *
     * @param capacity the capacity
     */
    public HierarchyRepository(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
    }

    private void removeNode(CacheEntry node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        }else{
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        }else{
            tail = node.prev;
        }
    }

    private void addToHead(CacheEntry node) {
        node.next = head;
        node.prev = null;
        if (head != null){
            head.prev = node;
        }
        head = node;
        if (tail == null){
            tail = head;
        }
    }

    private void moveToHead(CacheEntry node) {
        removeNode(node);
        addToHead(node);
    }

    /**
     * Contains key boolean.
     *
     * @param key the key
     * @return the boolean
     */
    public boolean containsKey(String key) {
        return cache.containsKey(key.toLowerCase());
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
            CacheEntry node = cache.get(lowerKey);
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
        CacheEntry newNode = new CacheEntry(lowerKey, category);
        cache.put(lowerKey, newNode);
        addToHead(newNode);

        if (cache.size() > capacity) {
            cache.remove(tail.key);
            removeNode(tail);
        }
    }

    /**
     * Remove boolean.
     *
     * @param key the key
     * @return the boolean
     */
    public boolean remove(String key) {
        String lowerKey = key.toLowerCase();
        if (cache.containsKey(lowerKey)) {
            CacheEntry node = cache.get(lowerKey);
            removeNode(node);
            cache.remove(lowerKey);
            return true;
        }
        return false;
    }

    /**
     * Gets all categories.
     *
     * @return the all categories
     */
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        CacheEntry current = head;
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