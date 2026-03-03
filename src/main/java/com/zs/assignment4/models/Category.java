package com.zs.assignment4.models;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The type Category.
 */
public class Category {
    private final String name;
    private final Map<String, SubCategory> subCategories;

    /**
     * Instantiates a new Category.
     *
     * @param name the name
     */
    public Category(String name) {
        this.name = name;
        this.subCategories = new LinkedHashMap<>();
    }

    /**
     * Gets name.
     *
     * @return the Category name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets sub categories.
     *
     * @return the sub categories LinkedHashMap
     */
    public Map<String, SubCategory> getSubCategories() {
        return subCategories;
    }

    /**
     * Add sub category.
     *
     * @param sc the sc
     */
    public void addSubCategory(SubCategory sc) {
        subCategories.put(sc.getName().toLowerCase(), sc);
    }
}