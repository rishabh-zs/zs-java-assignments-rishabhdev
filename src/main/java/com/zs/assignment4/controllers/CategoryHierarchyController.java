package com.zs.assignment4.controllers;

import com.zs.assignment4.services.HierarchyService;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * The type Category hierarchy controller.
 */
public class CategoryHierarchyController {
    private final HierarchyService service = new HierarchyService();
    private final Scanner sc;

    public CategoryHierarchyController(Scanner sc) {
        this.sc = sc;
    }

    /**
     * Start.
     */
    public void start() {
        runDemo();
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== Category Hierarchy Menu ===");
            System.out.println("1. Display Hierarchy");
            System.out.println("2. Search a Category");
            System.out.println("3. Delete a Category");
            System.out.println("4. Add a Category");
            System.out.println("5. Add a SubCategory");
            System.out.println("6. Delete a SubCategory");
            System.out.println("7. Add a Product");
            System.out.println("8. Delete a Product");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");

            try {
                String choice = sc.nextLine().trim();
                switch (choice) {
                    case "1" -> service.displayHierarchy();
                    case "2" -> searchCategory();
                    case "3" -> deleteCategory();
                    case "4" -> addCategory();
                    case "5" -> addSubCategory();
                    case "6" -> deleteSubCategory();
                    case "7" -> addProduct();
                    case "8" -> deleteProduct();
                    case "0" -> {
                        System.out.println("Back to Main Menu.");
                        exit = true;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (NoSuchElementException e) {
                System.out.println("\nInput stream closed. Exiting...");
                return;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void searchCategory() {
        System.out.print("Enter Category name to search: ");
        String name = sc.nextLine().trim();
        if (service.searchCategory(name)) {
            System.out.println("Category '" + name + "' exists in the hierarchy.");
        } else {
            System.out.println("Category '" + name + "' NOT found.");
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
            System.out.println("Category added.");
        } else {
            System.out.println("Category already exists.");
        }
    }

    private void addSubCategory() {
        System.out.print("Enter the Parent Category name: ");
        String catName = sc.nextLine().trim();
        System.out.print("Enter new SubCategory name: ");
        String subName = sc.nextLine().trim();

        if (service.addSubCategory(catName, subName)) {
            System.out.println("SubCategory added successfully.");
        } else {
            System.out.println("Parent Category not found.");
        }
    }

    private void deleteSubCategory() {
        System.out.print("Enter SubCategory name to delete: ");
        String name = sc.nextLine().trim();
        if (service.deleteSubCategory(name)) {
            System.out.println("SubCategory deleted successfully.");
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
        System.out.println("Product added successfully.");
    }

    private void deleteProduct() {
        System.out.print("Enter Product name to delete: ");
        String name = sc.nextLine().trim();
        if (service.deleteProduct(name)) {
            System.out.println("Product deleted successfully.");
        } else {
            System.out.println("Product not found.");
        }
    }

    /**
     * Run demo.
     */
    public void runDemo() {
        String[] categories = {"Electronics", "Fashion", "Home"};
        String[][] subCategories = {
                {"Mobiles", "Laptops"},
                {"Men", "Women"},
                {"Kitchen", "Furniture"}
        };
        String[][][] products = {
                {{"iPhone 16", "Samsung S25", "OnePlus 13"}, {"MacBook Air", "Dell XPS", "Lenovo ThinkPad"}},
                {{"Shirt", "Jeans", "Jacket"}, {"Dress", "Top", "Skirt"}},
                {{"Mixer", "Cooker", "Microwave"}, {"Sofa", "Table", "Chair"}}
        };

        for (int i = 0; i < categories.length; i++) {
            service.addCategory(categories[i]);
            for (int j = 0; j < subCategories[i].length; j++) {
                service.addSubCategory(categories[i], subCategories[i][j]);
                for (String product : products[i][j]) {
                    service.addProduct(categories[i], subCategories[i][j], product);
                }
            }
        }
    }


}
