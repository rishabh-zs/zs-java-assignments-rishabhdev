package com.zs.assignment3.controllers;

import com.zs.assignment3.services.ProductService;
import com.zs.assignment3.system.BabyProduct;
import com.zs.assignment3.system.Electronics;
import com.zs.assignment3.system.Grocery;
import com.zs.assignment3.system.PersonalCare;
import com.zs.assignment3.system.Product;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ProductController {
    private final ProductService productService = new ProductService();
    private final Scanner scanner = new Scanner(System.in);

    public void executeRequest() {
        while (true) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> listAll();
                case "2" -> addProductMenu();
                case "3" -> removeProductMenu();
                case "4" -> searchByIdMenu();
                case "5" -> searchByNameMenu();
                case "0" -> {
                    System.out.println("Exiting. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("==== PRODUCT CATALOGUE - MENU ====");
        System.out.println("1. List catalogue");
        System.out.println("2. Add product");
        System.out.println("3. Remove product by ID");
        System.out.println("4. Search product by ID");
        System.out.println("5. Search product by name");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }

    private void listAll() {
        List<Product> items = productService.fetchAllProducts();
        printGroceryCatalogue(items);
        printElectronicsCatalogue(items);
        printPersonalCareCatalogue(items);
        printBabyProductCatalogue(items);
    }

    private void addProductMenu() {
        System.out.println("\nChoose category to add:");
        System.out.println("1. Grocery");
        System.out.println("2. Electronics");
        System.out.println("3. Personal Care");
        System.out.println("4. Baby Product");
        System.out.print("Choice: ");
        String c = scanner.nextLine().trim();
        try {
            switch (c) {
                case "1" -> addGrocery();
                case "2" -> addElectronics();
                case "3" -> addPersonalCare();
                case "4" -> addBabyProduct();
                default -> System.out.println("Invalid category choice.");
            }
        } catch (InputMismatchException ime) {
            System.out.println("Invalid input: " + ime.getMessage());
            scanner.nextLine();
        }
    }

    private void addGrocery() {
        System.out.print("Enter ID( for Grocery Ex:G6,G7 etc): ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();
        double price = readDouble("Enter price: ");
        System.out.print("Enter brand: ");
        String brand = scanner.nextLine().trim();
        System.out.print("Enter expiry date (YYYY-MM-DD): ");
        String expiry = scanner.nextLine().trim();
        Grocery g = new Grocery(id, name, price, brand, expiry);
        productService.addProduct(g);
        System.out.println("Grocery added: " + g);
    }

    private void addElectronics() {
        System.out.print("Enter ID( for Electronics Ex:E6,E7 etc): ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();
        double price = readDouble("Enter price: ");
        System.out.print("Enter brand: ");
        String brand = scanner.nextLine().trim();
        int warranty = readInt("Enter warranty months: ");
        Electronics e = new Electronics(id, name, price, brand, warranty);
        productService.addProduct(e);
        System.out.println("Electronics added: " + e);
    }

    private void addPersonalCare() {
        System.out.print("Enter ID( for PersonalCare Products Ex:P6,P7 etc): ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();
        double price = readDouble("Enter price: ");
        System.out.print("Enter brand: ");
        String brand = scanner.nextLine().trim();
        PersonalCare p = new PersonalCare(id, name, price, brand);
        productService.addProduct(p);
        System.out.println("Personal care added: " + p);
    }

    private void addBabyProduct() {
        System.out.print("Enter ID( for BabyCare Product Ex:B6,B7 etc): ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();
        double price = readDouble("Enter price: ");
        System.out.print("Enter brand: ");
        String brand = scanner.nextLine().trim();
        System.out.print("Enter recommended age: ");
        String age = scanner.nextLine().trim();
        BabyProduct b = new BabyProduct(id, name, price, brand, age);
        productService.addProduct(b);
        System.out.println("Baby product added: " + b);
    }

    private void removeProductMenu() {
        System.out.print("Enter product ID to remove(Ex:Grocery-G1,G2 , Electronics-E1,E2 , BabyCare Products-B1,B2 , PersonalCare Product-P1,P2): ");
        String id = scanner.nextLine().trim();
        boolean removed = productService.removeProductById(id);
        if (removed) {
            System.out.println("Product removed: " + id);
        } else{
            System.out.println("Product not found: " + id);
        }
    }

    private void searchByIdMenu() {
        System.out.print("Enter product ID to search(Ex:Grocery-G1,G2 , Electronics-E1,E2 , BabyCare Products-B1,B2 , PersonalCare Product-P1,P2): ");
        String id = scanner.nextLine().trim();
        Product p = productService.findById(id);
        if(p == null) {
            System.out.println("No product found with ID: " + id);
        } else{
            System.out.println("Found: " + p);
        }
    }

    private void searchByNameMenu() {
        System.out.print("Enter name (or part) to search: ");
        String q = scanner.nextLine().trim();
        List<Product> found = productService.searchByName(q);
        if (found.isEmpty()) {
            System.out.println("No products match: " + q);
        } else {
            System.out.println("Matches:");
            int i = 1;
            for (Product p : found) {
                System.out.println(i + ". " + p);
                i++;
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException nfe) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException nfe) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    // Use toString() on the entity to avoid tight coupling on Grocery, Electronics, PersonalCare, and BabyProduct.
    private void printGroceryCatalogue(List<Product> items) {
        System.out.println("\n---------------Grocery--------------------");
        int count = 1;
        for (Product item : items) {
            if (item instanceof Grocery) {
                System.out.println(count + ". " + item);
                count++;
            }
        }
    }

    private void printElectronicsCatalogue(List<Product> items) {
        System.out.println("\n---------------Electronics--------------------");
        int count = 1;
        for (Product item : items) {
            if (item instanceof Electronics) {
                System.out.println(count + ". " + item);
                count++;
            }
        }
    }

    private void printPersonalCareCatalogue(List<Product> items) {
        System.out.println("\n---------------Personal Care--------------------");
        int count = 1;
        for (Product item : items) {
            if (item instanceof PersonalCare) {
                // Personal care products are non-returnable;
                System.out.println(count + ". " + item);
                count++;
            }
        }
    }

    private void printBabyProductCatalogue(List<Product> items) {
        System.out.println("\n---------------Baby Product--------------------");
        int count = 1;
        for (Product item : items) {
            if (item instanceof BabyProduct) {
                System.out.println(count + ". " + item);
                count++;
            }
        }
    }
}