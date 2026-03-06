package com.zs.assignment4.services;

import com.zs.assignment4.models.Category;
import com.zs.assignment4.models.Product;
import com.zs.assignment4.models.SubCategory;
import com.zs.assignment4.repositories.CategoryRepository;
import com.zs.assignment4.repositories.InMemoryCategoryRepository;
import com.zs.assignment4.repositories.LruCache;

/**
 * The type Hierarchy service.
 */
public class HierarchyService {
    private final LruCache cache;
    private final CategoryRepository repository;

    /**
     * Instantiates a new Hierarchy service.
     *
     * @param capacity the capacity
     */
    public HierarchyService(int capacity) {
        this.cache = new LruCache(capacity);
        this.repository = new InMemoryCategoryRepository();
    }

    private Category getCategoryThroughCache(String name) {
        Category cat = cache.get(name);
        if (cat != null) {
            return cat;
        }

        cat = repository.findByName(name);
        if (cat != null) {
            cache.put(name, cat);
        }
        return cat;
    }

    /**
     * Add category boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean addCategory(String name) {
        if (repository.existsByName(name)) {
            getCategoryThroughCache(name);
            return false;
        }
        Category newCat = new Category(name);
        repository.save(newCat);
        cache.put(name, newCat);
        return true;
    }

    /**
     * Search category.
     *
     * @param name the name
     * @return the category
     */
    public Category searchCategory(String name) {
        return getCategoryThroughCache(name);
    }

    /**
     * Delete category boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean deleteCategory(String name) {
        if (repository.deleteByName(name)) {
            cache.remove(name);
            return true;
        }
        return false;
    }

    /**
     * Add sub category boolean.
     *
     * @param catName    the cat name
     * @param subCatName the sub cat name
     * @return the boolean
     */
    public boolean addSubCategory(String catName, String subCatName) {
        Category cat = getCategoryThroughCache(catName);
        if (cat == null) {
            return false;
        }

        if (cat.getSubCategories().containsKey(subCatName.toLowerCase())) {
            throw new IllegalArgumentException("SubCategory already exists.");
        }
        cat.addSubCategory(new SubCategory(subCatName));
        cache.put(catName, cat);
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
        for (Category cat : repository.findAll()) {
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
        Category cat = getCategoryThroughCache(catName);
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
        cache.put(catName, cat);
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
        for (Category cat : repository.findAll()) {
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
        for (Category cat : repository.findAll()) {
            if (cat.getSubCategories().containsKey(key)) {
                getCategoryThroughCache(cat.getName());
                return cat.getSubCategories().get(key);
            }
        }
        return null;
    }

    /**
     * Search product.
     *
     * @param prodName the prod name
     * @return the product
     */
    public Product searchProduct(String prodName) {
        String key = prodName.toLowerCase();
        for (Category cat : repository.findAll()) {
            for (SubCategory subCat : cat.getSubCategories().values()) {
                if (subCat.getProducts().containsKey(key)) {
                    getCategoryThroughCache(cat.getName()); // Push parent category to MRU
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
        System.out.println("\n--- Cached Categories (From MRU to LRU) ---");
        if (cache.isEmpty()) {
            System.out.println("The cache is empty.");
        } else {
            for (Category cat : cache.getCachedCategories()) {
                printCategory(cat);
            }
        }
    }

    private void printCategory(Category cat) {
        System.out.println("- " + cat.getName());
        for (SubCategory subCat : cat.getSubCategories().values()) {
            System.out.println("  |-- " + subCat.getName());
            for (Product prod : subCat.getProducts().values()) {
                System.out.println("      |-- " + prod.getName());
            }
        }
    }
}