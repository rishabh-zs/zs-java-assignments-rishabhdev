package com.zs.assignment4.controllers;

import com.zs.assignment4.services.HierarchyService;
import com.zs.assignment4.models.Category;
import com.zs.assignment4.models.Product;
import com.zs.assignment4.models.SubCategory;

import java.util.Scanner;

public class CategoryHierarchyController {
    private HierarchyService service;
    private final Scanner sc = new Scanner(System.in);

    public void start() {
        System.out.println("---Default capacity for LRU Cache is set to 5.---");
        int capacity = 5;
        service = new HierarchyService(capacity);
        demo();

        boolean exit = false;
        try {
            while (!exit) {
                System.out.println("\n=== LRU Category Hierarchy Menu ===");
                System.out.println("1. Display Hierarchy");
                System.out.println("2. Search a Category");
                System.out.println("3. Delete a Category");
                System.out.println("4. Add a Category");
                System.out.println("5. Add a SubCategory");
                System.out.println("6. Delete a SubCategory");
                System.out.println("7. Add a Product");
                System.out.println("8. Delete a Product");
                System.out.println("9. Search a SubCategory");
                System.out.println("10. Search a Product");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");

                String choice = sc.nextLine().trim();

                try {
                    switch (choice) {
                        case "1" -> service.displayHierarchy();
                        case "2" -> searchCategory();
                        case "3" -> deleteCategory();
                        case "4" -> addCategory();
                        case "5" -> addSubCategory();
                        case "6" -> deleteSubCategory();
                        case "7" -> addProduct();
                        case "8" -> deleteProduct();
                        case "9" -> searchSubCategory();
                        case "10" -> searchProduct();
                        case "0" -> exit = true;
                        default -> System.out.println("Invalid choice. Try again.");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } finally {
            sc.close();
        }
    }

    private void demo() {
        service.addCategory("Electronics");
        service.addSubCategory("Electronics", "Mobiles");
        service.addSubCategory("Electronics", "Laptops");
        service.addProduct("Electronics", "Mobiles", "iPhone");
        service.addProduct("Electronics", "Mobiles", "Cell Phone");
        service.addProduct("Electronics", "Mobiles", "Phone");
        service.addProduct("Electronics", "Laptops", "Professional Laptop");
        service.addProduct("Electronics", "Laptops", "Gaming Laptop");
        service.addProduct("Electronics", "Laptops", "Normal Laptop");

        service.addCategory("Fashion");
        service.addSubCategory("Fashion", "Men");
        service.addSubCategory("Fashion", "Women");
        service.addProduct("Fashion", "Men", "T-Shirt");
        service.addProduct("Fashion", "Men", "Jeans");
        service.addProduct("Fashion", "Men", "Jacket");
        service.addProduct("Fashion", "Women", "Dress");
        service.addProduct("Fashion", "Women", "Top");
        service.addProduct("Fashion", "Women", "Skirt");

        service.addCategory("Groceries");
        service.addSubCategory("Groceries", "Fruits");
        service.addSubCategory("Groceries", "Vegetables");
        service.addProduct("Groceries", "Fruits", "Apple");
        service.addProduct("Groceries", "Fruits", "Banana");
        service.addProduct("Groceries", "Fruits", "Orange");
        service.addProduct("Groceries", "Vegetables", "Carrot");
        service.addProduct("Groceries", "Vegetables", "Potato");
        service.addProduct("Groceries", "Vegetables", "Tomato");

        service.addCategory("Books");
        service.addSubCategory("Books", "Fiction");
        service.addSubCategory("Books", "NonFiction");
        service.addProduct("Books", "Fiction", "1984");
        service.addProduct("Books", "Fiction", "Dune");
        service.addProduct("Books", "Fiction", "Hamlet");
        service.addProduct("Books", "NonFiction", "Sapiens");
        service.addProduct("Books", "NonFiction", "Educated");
        service.addProduct("Books", "NonFiction", "Atomic Habits");

        service.addCategory("Sports");
        service.addSubCategory("Sports", "Fitness");
        service.addSubCategory("Sports", "Outdoor");
        service.addProduct("Sports", "Fitness", "Dumbbell");
        service.addProduct("Sports", "Fitness", "Yoga Mat");
        service.addProduct("Sports", "Fitness", "Treadmill");
        service.addProduct("Sports", "Outdoor", "Football");
        service.addProduct("Sports", "Outdoor", "Cricket Bat");
        service.addProduct("Sports", "Outdoor", "Tennis Racket");
    }

    private void searchCategory() {
        System.out.print("Enter Category name to search: ");
        String name = sc.nextLine().trim();
        Category cat = service.searchCategory(name);
        if (cat != null) {
            System.out.println("Category '" + cat.getName() + "' found. (Moved to MRU)");
        } else {
            System.out.println("Category NOT found.");
        }
    }

    private void deleteCategory() {
        System.out.print("Enter Category name to delete: ");
        String name = sc.nextLine().trim();
        if (service.deleteCategory(name)) {
            System.out.println("Category deleted successfully.");
        } else {
            System.out.println("Category not found.");
        }
    }

    private void addCategory() {
        System.out.print("Enter new Category name: ");
        String name = sc.nextLine().trim();
        if (service.addCategory(name)) {
            System.out.println("Category added. (Set as MRU)");
        } else {
            System.out.println("Category already exists. (Moved to MRU)");
        }
    }

    private void addSubCategory() {
        System.out.print("Enter the Parent Category name: ");
        String catName = sc.nextLine().trim();
        System.out.print("Enter new SubCategory name: ");
        String subName = sc.nextLine().trim();

        if (service.addSubCategory(catName, subName)) {
            System.out.println("SubCategory added. (Parent Category moved to MRU)");
        } else {
            System.out.println("Parent Category not found.");
        }
    }

    private void deleteSubCategory() {
        System.out.print("Enter SubCategory name to delete: ");
        String name = sc.nextLine().trim();
        if (service.deleteSubCategory(name)) {
            System.out.println("SubCategory deleted.");
        } else {
            System.out.println("SubCategory not found.");
        }
    }

    private void addProduct() {
        System.out.print("Enter the Category name: ");
        String catName = sc.nextLine().trim();
        System.out.print("Enter the SubCategory name: ");
        String subName = sc.nextLine().trim();
        System.out.print("Enter Product name: ");
        String prodName = sc.nextLine().trim();

        service.addProduct(catName, subName, prodName);
        System.out.println("Product added. (Parent Category moved to MRU)");
    }

    private void deleteProduct() {
        System.out.print("Enter Product name to delete: ");
        String name = sc.nextLine().trim();
        if (service.deleteProduct(name)) {
            System.out.println("Product deleted.");
        } else {
            System.out.println("Product not found.");
        }
    }

    private void searchSubCategory() {
        System.out.print("Enter SubCategory name to search: ");
        String name = sc.nextLine().trim();
        SubCategory subCat = service.searchSubCategory(name);
        if (subCat != null) {
            System.out.println("SubCategory '" + subCat.getName() + "' found. (Parent Category moved to MRU)");
        } else {
            System.out.println("SubCategory NOT found.");
        }
    }

    private void searchProduct() {
        System.out.print("Enter Product name to search: ");
        String name = sc.nextLine().trim();
        Product prod = service.searchProduct(name);
        if (prod != null) {
            System.out.println("Product '" + prod.getName() + "' found. (Parent Category moved to MRU)");
        } else {
            System.out.println("Product NOT found.");
        }
    }
}