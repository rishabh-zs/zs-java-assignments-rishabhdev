package com.zs.assignment4.services;

import com.zs.assignment4.models.Category;
import com.zs.assignment4.models.Product;
import com.zs.assignment4.models.SubCategory;
import com.zs.assignment4.repositories.LruCacheRepository;

/**
 * The type Hierarchy service.
 */
public class HierarchyService {
    private final LruCacheRepository repository;

    /**
     * Instantiates a new Hierarchy service.
     *
     * @param capacity the capacity
     */
    public HierarchyService(int capacity) {
        this.repository = new LruCacheRepository(capacity);
    }

    /**
     * Add category boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean addCategory(String name) {
        if (repository.containsKey(name)) {
            repository.get(name);
            return false;
        }
        repository.put(name, new Category(name));
        return true;
    }

    /**
     * Search category category.
     *
     * @param name the name
     * @return the category
     */
    public Category searchCategory(String name) {
        return repository.get(name);
    }

    /**
     * Delete category boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean deleteCategory(String name) {
        return repository.remove(name);
    }

    /**
     * Add sub category boolean.
     *
     * @param catName    the cat name
     * @param subCatName the sub cat name
     * @return the boolean
     */
    public boolean addSubCategory(String catName, String subCatName) {
        Category cat = repository.get(catName);
        if (cat == null) {
            return false;
        }

        if (cat.getSubCategories().containsKey(subCatName.toLowerCase())) {
            throw new IllegalArgumentException("SubCategory already exists.");
        }
        cat.addSubCategory(new SubCategory(subCatName));
        return true;
    }

    /**
     * Delete sub category boolean.
     *
     * @param subCatName the sub cat name
     * @return the boolean
     */
    public boolean deleteSubCategory(String subCatName) {
        String key = subCatName.toLowerCase();
        for (Category cat : repository.getAllCategories()) {
            if (cat.getSubCategories().containsKey(key)) {
                cat.removeSubCategory(key);
                return true;
            }
        }
        return false;
    }

    /**
     * Add product boolean.
     *
     * @param catName    the cat name
     * @param subCatName the sub cat name
     * @param prodName   the prod name
     * @return the boolean
     */
    public boolean addProduct(String catName, String subCatName, String prodName) {
        Category cat = repository.get(catName);
        if (cat == null) {
            throw new IllegalArgumentException("Category not found.");
        }

        SubCategory subCat = cat.getSubCategories().get(subCatName.toLowerCase());
        if (subCat == null) {
            throw new IllegalArgumentException("SubCategory not found.");
        }

        if (subCat.getProducts().containsKey(prodName.toLowerCase())) {
            throw new IllegalArgumentException("Product already exists.");
        }

        subCat.addProduct(new Product(prodName));
        return true;
    }

    /**
     * Delete product boolean.
     *
     * @param prodName the prod name
     * @return the boolean
     */
    public boolean deleteProduct(String prodName) {
        String key = prodName.toLowerCase();
        for (Category cat : repository.getAllCategories()) {
            for (SubCategory subCat : cat.getSubCategories().values()) {
                if (subCat.getProducts().containsKey(key)) {
                    subCat.removeProduct(key);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Search sub category sub category.
     *
     * @param subCatName the sub cat name
     * @return the sub category
     */
    public SubCategory searchSubCategory(String subCatName) {
        String key = subCatName.toLowerCase();
        for (Category cat : repository.getAllCategories()) {
            if (cat.getSubCategories().containsKey(key)) {
                repository.get(cat.getName());
                return cat.getSubCategories().get(key);
            }
        }
        return null;
    }

    /**
     * Search product product.
     *
     * @param prodName the prod name
     * @return the product
     */
    public Product searchProduct(String prodName) {
        String key = prodName.toLowerCase();
        for (Category cat : repository.getAllCategories()) {
            for (SubCategory subCat : cat.getSubCategories().values()) {
                if (subCat.getProducts().containsKey(key)) {
                    repository.get(cat.getName());
                    return subCat.getProducts().get(key);
                }
            }
        }
        return null;
    }

    /**
     * Display hierarchy.
     */
    public void displayHierarchy() {
        if (repository.isEmpty()) {
            System.out.println("The cache/hierarchy is empty.");
            return;
        }
        System.out.println("\n--- Category Hierarchy (From MRU to LRU) ---");
        for (Category cat : repository.getAllCategories()) {
            System.out.println("- " + cat.getName());
            for (SubCategory subCat : cat.getSubCategories().values()) {
                System.out.println("  |-- " + subCat.getName());
                for (Product prod : subCat.getProducts().values()) {
                    System.out.println("      |-- " + prod.getName());
                }
            }
        }
    }
}