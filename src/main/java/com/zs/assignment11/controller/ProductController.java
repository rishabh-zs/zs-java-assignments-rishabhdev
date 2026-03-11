package com.zs.assignment11.controller;


import com.zs.assignment11.model.Product;
import com.zs.assignment11.service.ProductService;
import com.zs.assignment11.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private static final Logger log = LoggerUtil.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PutMapping("/addProduct")
    public String handleAddProduct(@RequestBody Product product){
        log.debug("/addProduct endpoint was called");
        productService.addProduct(product);
        return "Product added successfully.";
    }

    @GetMapping("/GetallProducts")
    public List<Product> handleGetAllProducts(){
        log.debug("/allProducts endpoint was called");
        return productService.getAllProducts();
    }

    @DeleteMapping("/deleteProduct")
    public String handleDeleteProductById(@RequestParam("id") Long productId){
        log.debug("/deleteProduct endpoint was called");
        productService.deleteProductById(productId);
        return "Product deleted successfully.";
    }
}
