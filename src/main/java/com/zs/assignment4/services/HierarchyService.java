package com.zs.assignment4.services;

import com.zs.assignment4.models.CacheEntry;
import com.zs.assignment4.models.Category;
import com.zs.assignment4.models.Product;
import com.zs.assignment4.models.SubCategory;

import java.util.HashMap;
import java.util.Map;


public class HierarchyService {
    private final int capacity;
    private final Map<String, CacheEntry> cache;

    private CacheEntry head;
    private CacheEntry tail;

    public HierarchyService(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
    }

    private void removeNode(CacheEntry node) {
        if (node.prev != null) node.prev.next = node.next;
        else head = node.next;

        if (node.next != null) node.next.prev = node.prev;
        else tail = node.prev;
    }

    private void addToHead(CacheEntry node) {
        node.next = head;
        node.prev = null;
        if (head != null) head.prev = node;
        head = node;
        if (tail == null) tail = head;
    }

    private void moveToHead(CacheEntry node) {
        removeNode(node);
        addToHead(node);
    }

    public boolean addCategory(String name) {
        String key = name.toLowerCase();
        if (cache.containsKey(key)) {
            moveToHead(cache.get(key));
            return false;
        }

        Category newCat = new Category(name);
        CacheEntry newNode = new CacheEntry(key, newCat);
        cache.put(key, newNode);
        addToHead(newNode);

        if (cache.size() > capacity) {
            cache.remove(tail.key);
            removeNode(tail);
        }
        return true;
    }

    public Category searchCategory(String name) {
        String key = name.toLowerCase();
        if (cache.containsKey(key)) {
            CacheEntry node = cache.get(key);
            moveToHead(node);
            return node.value;
        }
        return null;
    }

    public boolean deleteCategory(String name) {
        String key = name.toLowerCase();
        if (cache.containsKey(key)) {
            CacheEntry node = cache.get(key);
            removeNode(node);
            cache.remove(key);
            return true;
        }
        return false;
    }

    public boolean addSubCategory(String catName, String subCatName) {
        Category cat = searchCategory(catName);
        if (cat == null) {
            return false;
        }

        if (cat.getSubCategories().containsKey(subCatName.toLowerCase())) {
            throw new IllegalArgumentException("SubCategory already exists.");
        }
        cat.addSubCategory(new SubCategory(subCatName));
        return true;
    }

    public boolean deleteSubCategory(String subCatName) {
        String key = subCatName.toLowerCase();
        CacheEntry current = head;
        while (current != null) {
            Category cat = current.value;
            if (cat.getSubCategories().containsKey(key)) {
                cat.removeSubCategory(key);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public boolean addProduct(String catName, String subCatName, String prodName) {
        Category cat = searchCategory(catName); // Automatically moves to Head
        if (cat == null){
            throw new IllegalArgumentException("Category not found.");
        }

        SubCategory subCat = cat.getSubCategories().get(subCatName.toLowerCase());
        if (subCat == null){
            throw new IllegalArgumentException("SubCategory not found.");
        }

        if (subCat.getProducts().containsKey(prodName.toLowerCase())) {
            throw new IllegalArgumentException("Product already exists.");
        }

        subCat.addProduct(new Product(prodName));
        return true;
    }

    public boolean deleteProduct(String prodName) {
        String key = prodName.toLowerCase();
        CacheEntry current = head;
        while (current != null) {
            Category cat = current.value;
            for (SubCategory subCat : cat.getSubCategories().values()) {
                if (subCat.getProducts().containsKey(key)) {
                    subCat.removeProduct(key);
                    return true;
                }
            }
            current = current.next;
        }
        return false;
    }

    public SubCategory searchSubCategory(String subCatName) {
        String key = subCatName.toLowerCase();
        CacheEntry current = head;
        while (current != null) {
            Category cat = current.value;
            if (cat.getSubCategories().containsKey(key)) {
                moveToHead(current);
                return cat.getSubCategories().get(key);
            }
            current = current.next;
        }
        return null;
    }

    public Product searchProduct(String prodName) {
        String key = prodName.toLowerCase();
        CacheEntry current = head;
        while (current != null) {
            Category cat = current.value;
            for (SubCategory subCat : cat.getSubCategories().values()) {
                if (subCat.getProducts().containsKey(key)) {
                    moveToHead(current); // Activity detected, move parent to MRU
                    return subCat.getProducts().get(key);
                }
            }
            current = current.next;
        }
        return null;
    }

    public void displayHierarchy() {
        if (head == null) {
            System.out.println("The cache/hierarchy is empty.");
            return;
        }
        System.out.println("\n--- Category Hierarchy (From MRU to LRU) ---");
        CacheEntry current = head;
        while (current != null) {
            Category cat = current.value;
            System.out.println("- " + cat.getName());
            for (SubCategory subCat : cat.getSubCategories().values()) {
                System.out.println("  |-- " + subCat.getName());
                for (Product prod : subCat.getProducts().values()) {
                    System.out.println("      |-- " + prod.getName());
                }
            }
            current = current.next;
        }
        System.out.println("--------------------------------------------\n");
    }
}
