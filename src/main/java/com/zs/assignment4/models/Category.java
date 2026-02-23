package com.zs.assignment4.models;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private final String name;
    private final List<Category> subCategories;

    public Category(String name) {
        this.name = name;
        this.subCategories = new ArrayList<>();
    }

    // Getters and Setters
    public String getName() { return name; }
    public List<Category> getSubCategories() { return subCategories; }
}