package com.zs.assignment4.controllers;

import com.zs.assignment4.services.LRUCacheService;
import com.zs.assignment4.services.CategoryService;

public class AssignmentController {
    private final LRUCacheService<Integer, String> cache;
    private final CategoryService categoryService;

    public AssignmentController() {
        this.cache = new LRUCacheService<>(3);
        this.categoryService = new CategoryService("Amazon");
    }

    public void runDemo() {
        // Cache Operations
        System.out.println("=== LRU Cache Demo (Capacity: 3) ===");
        cache.put(1, "Electronics");
        cache.put(2, "Books");
        cache.put(3, "Clothing");
        System.out.println("Cache Get ID 1: " + cache.get(1));  // Access ID 1 (makes it MRU)

        cache.put(4, "Home & Kitchen");  // This evicts ID 2 (LRU)
        System.out.println("Cache Get ID 2 (after eviction): " + cache.get(2));  // Should be null
        System.out.println("Cache Get ID 3: " + cache.get(3));  // Should exist

        cache.put(5, "Sports");  // This evicts ID 4 (LRU)
        System.out.println("Cache Get ID 4 (after eviction): " + cache.get(4));  // Should be null
        System.out.println("Cache Get ID 5: " + cache.get(5));  // Should exist

        // Category Operations
        System.out.println("\n=== Category Hierarchy Demo ===");
        categoryService.addSubCategory("Amazon", "Electronics");
        categoryService.addSubCategory("Amazon", "Books");
        categoryService.addSubCategory("Amazon", "Clothing");

        categoryService.addSubCategory("Electronics", "Laptops");
        categoryService.addSubCategory("Electronics", "Smartphones");
        categoryService.addSubCategory("Electronics", "Tablets");

        categoryService.addSubCategory("Books", "Fiction");
        categoryService.addSubCategory("Books", "Non-Fiction");
        categoryService.addSubCategory("Books", "Technical");

        categoryService.addSubCategory("Laptops", "Dell");
        categoryService.addSubCategory("Laptops", "HP");
        categoryService.addSubCategory("Smartphones", "Apple");
        categoryService.addSubCategory("Smartphones", "Samsung");

        categoryService.printHierarchy(categoryService.getRoot(), 0);
    }
}