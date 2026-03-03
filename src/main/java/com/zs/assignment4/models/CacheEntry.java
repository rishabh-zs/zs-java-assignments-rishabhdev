package com.zs.assignment4.models;

/**
 * The type Cache entry.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 */
public class CacheEntry<K, V> {
    /**
     * The Key.
     */
    public K key;

    /**
     * The Value.
     */
    public V value;

    /**
     * The Prev.
     */
    public CacheEntry<K, V> prev;

    /**
     * The Next.
     */
    public CacheEntry<K,V> next;

    /**
     * Instantiates a new Cache entry.
     *
     * @param key   the key
     * @param value the value
     */
    public CacheEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }
}