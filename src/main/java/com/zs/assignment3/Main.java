package com.zs.assignment3;

import com.zs.assignment3.controllers.ProductController;

public class Main {
    public static void main(String[] args) {
        // Entry point for the application
        System.out.println("----------PRODUCT-CATALOGUE-----------");
        ProductController app = new ProductController();
        app.executeRequest();
    }
}