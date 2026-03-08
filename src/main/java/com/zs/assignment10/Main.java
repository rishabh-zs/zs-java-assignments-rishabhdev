package com.zs.assignment10;

import com.zs.assignment10.controllers.ProductController;
import com.zs.assignment10.dao.ProductDaoJdbcImpl;
import com.zs.assignment10.services.ProductService;

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
        ProductDaoJdbcImpl productDaoJdbcImpl = new ProductDaoJdbcImpl();
        ProductService productService = new ProductService(productDaoJdbcImpl);
        ProductController controller = new ProductController(productService);
        controller.start();
    }
}
