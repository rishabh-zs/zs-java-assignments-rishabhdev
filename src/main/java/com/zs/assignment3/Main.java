package com.zs.assignment3;

import com.zs.assignment3.controllers.ProductController;

/**
 * The type Main.
 */
public class Main {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        System.out.println("----------PRODUCT-CATALOGUE-----------");
        ProductController app = new ProductController();
        app.executeRequest();
    }
}