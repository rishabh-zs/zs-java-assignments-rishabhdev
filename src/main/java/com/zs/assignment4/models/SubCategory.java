package com.zs.assignment4.models;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The type Sub category.
 */
public class SubCategory {
    private final String name;
    private final Map<String, Product> products;

    /**
     * Instantiates a new Sub category.
     *
     * @param name the name
     */
    public SubCategory(String name) {
        this.name = name;
        this.products = new LinkedHashMap<>();
    }

    /**
     * Gets name.
     *
     * @return the Subcategory name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets products.
     *
     * @return the products
     */
    public Map<String, Product> getProducts() {
        return products;
    }

    /**
     * Add product.
     *
     * @param p the p
     */
    public void addProduct(Product p) {
        products.put(p.getName().toLowerCase(), p);
    }
}