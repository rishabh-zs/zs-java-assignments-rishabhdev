package com.zs.assignment10.controllers;

import com.zs.assignment10.model.Product;
import com.zs.assignment10.services.ProductService;
import java.util.List;

/**
 * The type Product controller.
 */
public class ProductController {

    private final ProductService productService;

    /**
     * Instantiates a new Product controller.
     *
     * @param productService the product service
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Display all products.
     */
    public void displayAllProducts() {
        System.out.println("\n--> Fetching all products...");
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("<-- Database is empty.");
        } else {
            products.forEach(p -> System.out.println("<-- " + p));
        }
    }

    /**
     * Display product.
     *
     * @param id the id
     */
    public void displayProduct(Integer id) {
        try {
            System.out.println("\n--> Fetching product ID: " + id);
            Product product = productService.getProduct(id);
            if (product != null) {
                System.out.println("<-- Found: " + product);
            } else {
                System.out.println("<-- Not Found: No product exists with ID " + id);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("<-- Error: " + e.getMessage());
        }
    }

    /**
     * Handle save product.
     *
     * @param id    the id
     * @param name  the name
     * @param price the price
     */
    public void handleSaveProduct(Integer id, String name, Double price) {
        try {
            System.out.println("\n--> Saving product: '" + name + "'");
            Product product = new Product(id, name, price);
            Product savedProduct = productService.saveProduct(product);
            if(savedProduct != null) {
                System.out.println("<-- Success! Saved to DB: " + savedProduct);
            }else{
                System.out.println("<-- Failed to save product.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("<-- Error: " + e.getMessage());
        }
    }

    /**
     * Handle delete product.
     *
     * @param id the id
     */
    public void handleDeleteProduct(Integer id) {
        try {
            System.out.println("\n--> Deleting product ID: " + id);
            boolean deleted = productService.deleteProduct(id);
            if (deleted) {
                System.out.println("<-- Success! Product deleted.");
            } else {
                System.out.println("<-- Failed to delete product.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("<-- Error: " + e.getMessage());
        }
    }

    /**
     * Handle clean up boolean.
     *
     * @return the boolean
     */
    public boolean handleCleanUp() {
        System.out.println("\n--> Cleaning up database...");
        if (productService.cleanUp()) {
            System.out.println("<-- Database reset successful.");
            return true;
        }else{
            System.out.println("<-- Database reset failed.");
            return false;
        }
    }

    /**
     * Start the program.
     */
    public void start() {
        if(!handleCleanUp()){
            System.out.println("Aborting tests due to cleanup failure.");
            return;
        };
        System.out.println("---Integrated Testing on Products Table ---");

        ProductController productController = this;
        productController.handleSaveProduct(null, "Gaming Laptop", 1200.50);
        productController.handleSaveProduct(null, "Wireless Mouse", 45.00);

        productController.displayAllProducts();

        productController.handleSaveProduct(1, "Gaming Laptop Pro", 1450.00);
        productController.displayProduct(1);

        productController.handleDeleteProduct(2);
        productController.displayAllProducts();
    }
}
