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

    private Category getCategory(String name) {
        Category cat = cache.get(name);
        if (cat != null) {
            return cat;
        }
        cat = repository.findByName(name);
        if(cat != null){
            cache.put(name, cat);
            return cat;
        }else{
            return null;
        }
    }

    /**
     * Add category category.
     *
     * @param name the name
     * @return the category
     */
    public Category addCategory(String name) {
        if (getCategory(name)!=null) {
            return null;
        }else{
            Category newCat = new Category(name);
            repository.save(newCat);
            cache.put(name, newCat);
            return newCat;
        }
    }

    /**
     * Search category.
     *
     * @param name the name
     * @return the category
     */
    public Category searchCategory(String name) {
        return getCategory(name);
    }

    /**
     * Delete category.
     *
     * @param name the name
     * @return the deleted category
     */
    public Category deleteCategory(String name) {
        Category deleted = repository.deleteByName(name);
        if (deleted != null) {
            cache.remove(name);
        }
        return deleted;
    }

    /**
     * Add sub category boolean.
     *
     * @param catName    the cat name
     * @param subCatName the sub cat name
     * @return the boolean
     */
    public SubCategory addSubCategory(String catName, String subCatName) {
        Category cat = getCategory(catName);
        if (cat == null) {
            return null;
        }

        if (cat.getSubCategories().containsKey(subCatName.toLowerCase())) {
            throw new IllegalArgumentException("SubCategory already exists.");
        }
        cat.addSubCategory(new SubCategory(subCatName));
        cache.put(catName, cat);
        return cat.getSubCategories().get(subCatName.toLowerCase());
    }

    /**
     * Delete sub category boolean.
     *
     * @param subCatName the sub cat name
     * @return the boolean
     */
    public SubCategory deleteSubCategory(String subCatName) {
        String key = subCatName.toLowerCase();
        for (Category cat : repository.findAll()) {
            if (cat.getSubCategories().containsKey(key)) {
                cat.removeSubCategory(key);
                return cat.getSubCategories().get(key);
            }
        }
        return null;
    }

    /**
     * Add product boolean.
     *
     * @param catName    the cat name
     * @param subCatName the sub cat name
     * @param prodName   the prod name
     * @return the boolean
     */
    public Product addProduct(String catName, String subCatName, String prodName) {
        Category cat = getCategory(catName);
        if (cat == null) {
            return null;
        }

        SubCategory subCat = cat.getSubCategories().get(subCatName.toLowerCase());
        if (subCat == null) {
            return null;
        }

        if (subCat.getProducts().containsKey(prodName.toLowerCase())) {
            return null;
        }

        subCat.addProduct(new Product(prodName));
        cache.put(catName, cat);
        return subCat.getProducts().get(prodName.toLowerCase());
    }

    /**
     * Delete product boolean.
     *
     * @param prodName the prod name
     * @return the boolean
     */
    public Product deleteProduct(String prodName) {
        String key = prodName.toLowerCase();
        for (Category cat : repository.findAll()) {
            for (SubCategory subCat : cat.getSubCategories().values()) {
                if (subCat.getProducts().containsKey(key)) {
                    subCat.removeProduct(key);
                    return subCat.getProducts().get(key);
                }
            }
        }
        return null;
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
                getCategory(cat.getName());
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
                    getCategory(cat.getName()); // Push parent category to MRU
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

    /**
     * Print category.
     *
     * @param cat the cat
     */
    public void printCategory(Category cat) {
        System.out.println("- " + cat.getName());
        for (SubCategory subCat : cat.getSubCategories().values()) {
            System.out.println("  |-- " + subCat.getName());
            for (Product prod : subCat.getProducts().values()) {
                System.out.println("      |-- " + prod.getName());
            }
        }
    }
}
