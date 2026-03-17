package com.zs.assignment10.controllers;

import com.zs.assignment10.model.Product;
import com.zs.assignment10.services.ProductService;

import java.util.List;
import java.util.Scanner;

import lombok.extern.slf4j.Slf4j;


/**
 * The type Product controller.
 */
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final Scanner scanner;

    /**
     * Instantiates a new Product controller.
     *
     * @param productService the product service
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
        this.scanner = new Scanner(System.in);
    }


    /**
     * Instantiates a new Product controller.
     *
     * @param productService the product service
     * @param scanner        the scanner
     */
    ProductController(ProductService productService, Scanner scanner) {
        this.productService = productService;
        this.scanner = scanner;
    }

    /**
     * Display all products.
     */
    public void displayAllProducts() {
        logger.info("\n--> Fetching all products...");
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            logger.info("<-- No products found!");
        } else {
            logger.info("<-- Found {} products", products.size());
            products.forEach(p -> logger.info("<-- {}", p));
        }
    }

    /**
     * Handle display product.
     */
    public void handleDisplayProduct() {
        System.out.print("Enter product ID to display: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        logger.info("\n--> Fetching product with ID: {}", id);
        Product product = productService.getProduct(id);
        if (product != null) {
            logger.info("<-- Found: {}", product);
        } else {
            logger.info("<-- Not Found: No product exists with ID {}", id);
        }
    }

    /**
     * Handle delete product.
     */
    private void handleDeleteProduct() {
        System.out.print("Enter product ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        logger.info("\n--> Deleting product with ID: {}", id);
        Product deleted = productService.deleteProduct(id);
        if (deleted != null) {
            logger.info("<-- Success! Deleted: {}", deleted);
        } else {
            logger.info("<-- Failed to delete product.");
        }
    }

    /**
     * Handle insert product.
     */
    private void handleInsertProduct() {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter product price: ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        logger.info("\n--> Inserting product: {}", name);
        Product inserted = productService.insertProduct(new Product(null, name, price));
        if (inserted != null) {
            logger.info("<-- Success! Inserted: {}", inserted);
        } else {
            logger.info("<-- Failed to insert product.");
        }
    }

    /**
     * Handle update product.
     */
    private void handleUpdateProduct() {
        System.out.print("Enter product ID to update: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter new product name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter new product price: ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        logger.info("\n--> Updating product with ID: {}", id);
        Product updated = productService.updateProduct(new Product(id, name, price));
        if (updated != null) {
            logger.info("<-- Success! Updated: {}", updated);
        } else {
            logger.info("<-- Failed to update product (product may not exist).");
        }
    }

    /**
     * Handle clean up boolean.
     *
     * @return the boolean
     */
    public boolean handleCleanUp() {
        logger.info("\n--> Ensuring products table exists...");
        if (productService.cleanUp()) {
            logger.info("<-- Products table is ready.");
            return true;
        } else {
            System.out.println("<-- Failed to prepare products table.");
            return false;
        }
    }

    /**
     * Show menu int.
     *
     * @return the int
     */
    public int showMenu() {
        System.out.println("\n========== Products Menu ==========");
        System.out.println(" 1. Display all products");
        System.out.println(" 2. Find a product by ID");
        System.out.println(" 3. Insert a new product");
        System.out.println(" 4. Update an existing product");
        System.out.println(" 5. Delete a product");
        System.out.println(" 0. Exit");
        System.out.println("===================================");
        System.out.print("Choose an option: ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Start the Application.
     */
    public void start() {
        if (!handleCleanUp()) {
            System.out.println("Aborting because the products table is unavailable.");
            return;
        }

        boolean running = true;
        while (running) {
            int choice = showMenu();
            try {
                switch (choice) {
                    case 1 -> displayAllProducts();
                    case 2 -> handleDisplayProduct();
                    case 3 -> handleInsertProduct();
                    case 4 -> handleUpdateProduct();
                    case 5 -> handleDeleteProduct();
                    case 0 -> {
                        logger.info("Exiting. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Invalid option. Please enter a number between 0 and 5.");
                }
            } catch (IllegalArgumentException e) {
                logger.error("<-- Input error: {}", e.getMessage());
            } catch (RuntimeException e) {
                logger.error("<-- Unexpected error: {}", e.getMessage());
            }
        }
    }
}
