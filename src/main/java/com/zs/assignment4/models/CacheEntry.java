package com.zs.assignment4.models;

public class CacheEntry<K, V> {
    public K key;
    public V value;
    public CacheEntry<K, V> prev, next;

    public CacheEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }
}