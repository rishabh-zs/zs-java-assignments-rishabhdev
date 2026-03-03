package com.zs.assignment4.services;

import com.zs.assignment4.models.Category;
import com.zs.assignment4.models.Product;
import com.zs.assignment4.models.SubCategory;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The type Hierarchy service.
 */
public class HierarchyService {
    private final Map<String, Category> rootCategories = new LinkedHashMap<>();

    /**
     * Display hierarchy.
     */
    public void displayHierarchy() {
        if (rootCategories.isEmpty()) {
            System.out.println("The hierarchy is empty.");
            return;
        }
        System.out.println("\n--- Category Hierarchy ---");
        for (Category cat : rootCategories.values()) {
            System.out.println("- " + cat.getName());
            for (SubCategory subCat : cat.getSubCategories().values()) {
                System.out.println("  |-- " + subCat.getName());
                for (Product prod : subCat.getProducts().values()) {
                    System.out.println("      |-- " + prod.getName());
                }
            }
        }
        System.out.println("--------------------------\n");
    }

    /**
     * Search category boolean.
     *
     * @param catName the cat name
     * @return the boolean
     */
    public boolean searchCategory(String catName) {
        return rootCategories.containsKey(catName.toLowerCase());
    }

    /**
     * Delete category boolean.
     *
     * @param catName the cat name
     * @return the boolean
     */
    public boolean deleteCategory(String catName) {
        if (searchCategory(catName)) {
            rootCategories.remove(catName.toLowerCase());
            return true;
        }
        return false;
    }

    /**
     * Add category boolean.
     *
     * @param catName the cat name
     * @return the boolean
     */
    public boolean addCategory(String catName) {
        if (searchCategory(catName)) {
            return false;
        }
        rootCategories.put(catName.toLowerCase(), new Category(catName));
        return true;
    }

    /**
     * Add sub category boolean.
     *
     * @param catName    the cat name
     * @param subCatName the sub cat name
     * @return the boolean
     */
    public boolean addSubCategory(String catName, String subCatName) {
        Category cat = rootCategories.get(catName.toLowerCase());
        if (cat == null){
            return false;
        }

        if (cat.getSubCategories().containsKey(subCatName.toLowerCase())) {
            throw new IllegalArgumentException("SubCategory already exists in this category.");
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
        for (Category cat : rootCategories.values()) {
            if (cat.getSubCategories().containsKey(key)) {
                cat.getSubCategories().remove(key);
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
        Category cat = rootCategories.get(catName.toLowerCase());
        if (cat == null){
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
        for (Category cat : rootCategories.values()) {
            for (SubCategory subCat : cat.getSubCategories().values()) {
                if (subCat.getProducts().containsKey(key)) {
                    subCat.getProducts().remove(key);
                    return true;
                }
            }
        }
        return false;
    }
}