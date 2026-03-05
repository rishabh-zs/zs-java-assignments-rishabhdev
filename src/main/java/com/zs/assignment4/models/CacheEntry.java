package com.zs.assignment4.models;


/**
 * The type Cache entry.
 */
public class CacheEntry {
    /**
     * The Key.
     */
    public String key;
    /**
     * The Value.
     */
    public Category value;
    /**
     * The Prev.
     */
    public CacheEntry prev;
    /**
     * The Next.
     */
    public CacheEntry next;

    /**
     * Instantiates a new Cache entry.
     *
     * @param key   the key
     * @param value the value
     */
    public CacheEntry(String key, Category value) {
        this.key = key;
        this.value = value;
    }
}