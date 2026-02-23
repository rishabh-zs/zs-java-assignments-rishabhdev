package com.zs.assignment4.services;

import com.zs.assignment4.models.Category;

public class CategoryService {
    private final Category root;

    public CategoryService(String rootName) {
        this.root = new Category(rootName);
    }

    public void addSubCategory(String parentName, String childName) {
        Category parent = search(root, parentName);
        if (parent != null) {
            parent.getSubCategories().add(new Category(childName));
        }
    }

    public Category search(Category current, String name) {
        if (current.getName().equalsIgnoreCase(name)) return current;
        for (Category sub : current.getSubCategories()) {
            Category found = search(sub, name);
            if (found != null) return found;
        }
        return null;
    }

    public void printHierarchy(Category node, int level) {
        System.out.println("  ".repeat(level) + "└── " + node.getName());
        for (Category sub : node.getSubCategories()) {
            printHierarchy(sub, level + 1);
        }
    }

    public Category getRoot() { return root; }
}