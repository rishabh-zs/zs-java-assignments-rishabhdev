package com.zs.assignment11.controller;

import com.zs.assignment11.model.Category;
import com.zs.assignment11.model.Product;
import com.zs.assignment11.service.CategoryService;
import com.zs.assignment11.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private static final Logger log = LoggerUtil.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/stubApi/GetAllCategories")
    public List<Category> stubsGetAllCategories(){
        log.info("/stubApi/GetAllCategories endpoint was called");
        List<Category> categories=new ArrayList<>();
        categories.add(new Category(1,"electronics"));
        categories.add(new Category(2,"Fashion"));
        categories.add(new Category(3,"Sports"));
        return categories;
    }

    @GetMapping("/GetallCategories")
    public List<Category> handleGetAllCategories(){
        log.debug("/GetallCategories endpoint was called");
        return categoryService.getAllCategories();
    }

    @GetMapping("/stubApi/{categoryId}/products")
    public List<Product>  stubsGetProductsByCategoryId(@PathVariable Long categoryId){
        log.info("/stubApi/GetProductsByCategoryId endpoint was called");
        if(categoryId==1){
            List<Product> products=new ArrayList<>();
            products.add(new Product(1,"laptop",1000.0,1));
            products.add(new Product(2,"tv",2000.0,1));
            products.add(new Product(3,"iPhone",200.0,1));
            return products;
        }else if(categoryId==2){
            List<Product> products=new ArrayList<>();
            products.add(new Product(4,"FaceCream",4.50,2));
            products.add(new Product(5,"faceGel",10.0,2));
            return products;
        }
        return null;
    }

    @GetMapping("/{categoryId}/products")
    public List<Product> handleGetAllProductByCategoryId(@PathVariable Long categoryId){
        log.debug("/{categoryId}/products endpoint was called");
        return categoryService.getProductsByCategoryId(categoryId);
    }


}
