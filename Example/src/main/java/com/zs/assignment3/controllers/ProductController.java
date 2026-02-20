package com.zs.assignment3.controllers;

import com.zs.assignment3.services.ProductService;
import com.zs.assignment3.system.*;
import java.util.List;

public class ProductController {
    private final ProductService productService = new ProductService();

    public void executeRequest() {
        List<Product> items = productService.fetchAllProducts();

        // Calling specific print functions for each category
        printGroceryCatalogue(items);
        printElectronicsCatalogue(items);
        printPersonalCareCatalogue(items);
        printBabyProductCatalogue(items);
    }

    private void printGroceryCatalogue(List<Product> items) {
        System.out.println("\n---------------Grocery--------------------");
        int count = 1;
        for (Product item : items) {
            if (item instanceof Grocery g) {
                System.out.println(count + ". " + g.getName() + " | Brand: " + g.getBrand() +
                        " | Price: $" + g.getPrice() + " | Expiry: " + g.getExpiryDate());
                count++;
            }
        }
    }

    private void printElectronicsCatalogue(List<Product> items) {
        System.out.println("\n---------------Electronics--------------------");
        int count = 1;
        for (Product item : items) {
            if (item instanceof Electronics e) {
                System.out.println(count + ". " + e.getName() + " | Brand: " + e.getBrand() +
                        " | Price: $" + e.getPrice() + " | Warranty: " + e.getWarrantyMonths() + " months");
                count++;
            }
        }
    }

    private void printPersonalCareCatalogue(List<Product> items) {
        System.out.println("\n---------------Personal Care--------------------");
        int count = 1;
        for (Product item : items) {
            if (item instanceof PersonalCare p) {
                // Personal care products are non-returnable
                System.out.println(count + ". " + p.getName() + " | Brand: " + p.getBrand() +
                        " | Price: $" + p.getPrice() + " | Returnable: No");
                count++;
            }
        }
    }

    private void printBabyProductCatalogue(List<Product> items) {
        System.out.println("\n---------------Baby Product--------------------");
        int count = 1;
        for (Product item : items) {
            if (item instanceof BabyProduct b) {
                System.out.println(count + ". " + b.getName() + " | Brand: " + b.getBrand() +
                        " | Price: $" + b.getPrice() + " | Age: " + b.getRecommendedAge());
                count++;
            }
        }
    }
}