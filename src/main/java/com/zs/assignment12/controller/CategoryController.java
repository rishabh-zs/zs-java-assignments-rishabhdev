package com.zs.assignment12.controller;

import com.zs.assignment12.model.Category;
import com.zs.assignment12.model.Product;
import com.zs.assignment12.service.CategoryService;
import com.zs.assignment12.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private static final Logger log = LoggerUtil.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }


    @PutMapping("/addCategory")
    public String handleAddCategory(@RequestBody Category category){
        log.debug("/addCategory endpoint was called");
        categoryService.addCategory(category);
        return "Category added successfully.";
    }

    @GetMapping("/GetallCategories")
    public List<Category> handleGetAllCategories(){
        log.debug("/GetallCategories endpoint was called");
        return categoryService.getAllCategories();
    }

    @GetMapping("/{categoryId}/products")
    public List<Product> handleGetAllProductByCategoryId(@PathVariable Long categoryId){
        log.debug("/{categoryId}/products endpoint was called");
        return categoryService.getProductsByCategoryId(categoryId);
    }

}
