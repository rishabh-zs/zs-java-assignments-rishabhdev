package com.zs.assignment11.controller;


import com.zs.assignment11.model.Product;
import com.zs.assignment11.service.ProductService;
import com.zs.assignment11.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private static final Logger log = LoggerUtil.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }


    @GetMapping("/stubApi/GetallProducts")
    public List<Product> stubApiGetAllProducts(){
        log.info("/stubApi/GetallProducts endpoint was called");
        List<Product> products=new ArrayList<>();
        products.add(new Product(1,"laptop",1000.0,1));
        products.add(new Product(2,"tv",2000.0,1));
        products.add(new Product(3,"iPhone",200.0,1));
        products.add(new Product(4,"FaceCream",4.50,2));
        products.add(new Product(5,"faceGel",10.0,2));
        return products;
    }

    @GetMapping("/GetallProducts")
    public List<Product> handleGetAllProducts(){
        log.debug("/allProducts endpoint was called");
        return productService.getAllProducts();
    }
}
