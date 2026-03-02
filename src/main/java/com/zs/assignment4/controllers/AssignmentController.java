package com.zs.assignment4.controllers;

import com.zs.assignment4.models.CacheEntry;
import com.zs.assignment4.services.LRUCacheService;
import com.zs.assignment4.services.CategoryService;
import java.util.Scanner;

public class AssignmentController {
    private final LRUCacheService<Integer, String> cache;
    private final CategoryService categoryService;

    public AssignmentController() {
        this.cache = new LRUCacheService<>(3);
        this.categoryService = new CategoryService("Amazon");
    }

    public void LruCacheOperation() {
        System.out.println("----------LRU_CACHE_OPERATIONS----------");
        Scanner sc = new Scanner(System.in);
        while (true) {
            printLruMenu();
            Integer choice = readInteger(sc, "Choose an option: ");
            if (choice == null) {
                System.out.println("\nInput stream closed. Exiting...");
                return;
            }

            try {
                switch (choice) {
                    case 1:
                        handlePrintFullCache();
                        break;
                    case 2:
                        handlePutCacheEntry(sc);
                        break;
                    case 3:
                        handleAddToHead(sc);
                        break;
                    case 4:
                        handleRemoveFromTail();
                        break;
                    case 5:
                        handleGetValueByKey(sc);
                        break;
                    case 6:
                        handleRemoveEntry(sc);
                        break;
                    case 7:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (IllegalStateException e) {
                System.out.println("\n" + e.getMessage());
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error while performing cache operation: " + e.getMessage());
            } finally{
                sc.close();
            }
        }
    }

    private void printLruMenu() {
        System.out.println("\n1. Print full LRU Cache");
        System.out.println("2. Put (Add/Update) Cache Entry");
        System.out.println("3. Add to head of LRU Cache");
        System.out.println("4. Remove from tail of LRU Cache");
        System.out.println("5. Get value by key from LRU Cache");
        System.out.println("6.remove entry from LRU Cache");
        System.out.println("7. Exit");
    }

    private void handlePrintFullCache() {
        System.out.println("Current Cache (MRU -> LRU): " + cache.getCacheDisplay());
    }

    private void handlePutCacheEntry(Scanner sc) {
        Integer key = readInteger(sc, "Enter key (Integer): ");
        if (key == null) {
            throw new IllegalStateException("Input stream closed. Exiting...");
        }

        String value = readRequiredString(sc, "Enter value (String): ");
        if (value == null) {
            throw new IllegalStateException("Input stream closed. Exiting...");
        }

        cache.put(key, value);
        System.out.println("Entry added/updated in cache.");
    }

    private void handleAddToHead(Scanner sc) {
        Integer key = readInteger(sc, "Enter key (Integer): ");
        if (key == null) {
            throw new IllegalStateException("Input stream closed. Exiting...");
        }

        String value = readRequiredString(sc, "Enter value (String): ");
        if (value == null) {
            throw new IllegalStateException("Input stream closed. Exiting...");
        }

        cache.addToHead(key, value);
        System.out.println("Entry moved/added to head.");
    }

    private void handleRemoveFromTail() {
        CacheEntry<Integer, String> removed = cache.removeFromTail();
        if (removed == null) {
            System.out.println("Cache is empty. Nothing to remove.");
            return;
        }
        System.out.println("Removed LRU entry: key=" + removed.key + ", value=" + removed.value);
    }

    private void handleGetValueByKey(Scanner sc) {
        Integer key = readInteger(sc, "Enter key to retrieve: ");
        if (key == null) {
            throw new IllegalStateException("Input stream closed. Exiting...");
        }

        String value = cache.get(key);
        if (value == null) {
            System.out.println("Key not found in cache.");
            return;
        }
        System.out.println("Value: " + value);
    }

    private void handleRemoveEntry(Scanner sc) {
        Integer key = readInteger(sc, "Enter key of entry to remove: ");
        if (key == null) {
            throw new IllegalStateException("Input stream closed. Exiting...");
        }

        CacheEntry<Integer, String> removed = cache.removeEntry(key);
        if (removed == null) {
            System.out.println("Entry not found in cache.");
            return;
        }
        System.out.println("Removed entry: key=" + removed.key + ", value=" + removed.value);
    }

    private Integer readInteger(Scanner sc, String prompt) {
        while (true) {
            String input = readLine(sc, prompt);
            if (input == null) {
                return null;
            }

            String normalized = input.trim();
            if (normalized.isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a valid integer.");
                continue;
            }

            try {
                return Integer.parseInt(normalized);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private String readRequiredString(Scanner sc, String prompt) {
        while (true) {
            String input = readLine(sc, prompt);
            if (input == null) {
                return null;
            }

            String normalized = input.trim();
            if (normalized.isEmpty()) {
                System.out.println("Value cannot be empty. Please try again.");
                continue;
            }
            return normalized;
        }
    }

    private String readLine(Scanner sc, String prompt) {
        System.out.print(prompt);
        if (!sc.hasNextLine()) {
            return null;
        }
        return sc.nextLine();
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

