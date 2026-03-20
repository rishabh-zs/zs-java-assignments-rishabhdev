package com.zs.assignment11.controller;

import com.zs.assignment11.model.Product;
import com.zs.assignment11.service.ProductService;
import com.zs.assignment11.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Product controller.
 */
@RestController
@RequestMapping("/products")
public class ProductController {
    private static final Logger log = LoggerUtil.getLogger(ProductController.class);
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
     * Handle get all products map.
     *
     * @return the map
     */
    @GetMapping("/GetallProducts")
    public Map<String, Object> handleGetAllProducts() {
        log.debug("/allProducts endpoint was called");
        List<Product> products = productService.getAllProducts();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "all product fetched successfully");
        response.put("products", products);
        response.put("totalProductCount", products.size());
        return response;
    }

    /**
     * Handle add product map.
     *
     * @param product the product
     * @return the map
     */
    @PostMapping("/aProduct")
    public Map<String, Object> handleAddProduct(@RequestBody Product product) {
        log.debug("/addProduct endpoint was called");
        Product addedProduct = productService.addProduct(product);

        Map<String, Object> response = new LinkedHashMap<>();
        Integer id = addedProduct.getId();
        response.put("status", "success");
        response.put("message", "Product with ID :" + id + " added successfully");
        response.put("addedProduct", addedProduct);
        return response;
    }

    /**
     * Handle delete product map.
     *
     * @param productId the body
     * @return the map
     */
    @DeleteMapping("/dProduct/{productId}")
    public Map<String, Object> handleDeleteProduct(@PathVariable Integer productId) {
        log.debug("/deleteProduct endpoint was called");
        Product deletedProduct = productService.deleteProduct(productId);

        Map<String, Object> response = new LinkedHashMap<>();
        Integer id = deletedProduct.getId();
        response.put("status", "success");
        response.put("message", "product with ID :" + id + " deleted successfully");
        response.put("deletedProduct", deletedProduct);
        return response;
    }

    /**
     * Handle update product map.
     *
     * @param product the product
     * @return the map
     */
    @PatchMapping("/uProduct")
    public Map<String, Object> handleUpdateProduct(@RequestBody Product product) {
        log.debug("/updateProduct endpoint was called");
        Product updatedProduct = productService.updateProduct(product);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "product with " + updatedProduct.getId() + " updated successfully");
        return response;
    }
}
