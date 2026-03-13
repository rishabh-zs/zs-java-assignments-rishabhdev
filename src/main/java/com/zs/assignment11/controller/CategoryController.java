package com.zs.assignment11.controller;

import com.zs.assignment11.model.Category;
import com.zs.assignment11.model.Product;
import com.zs.assignment11.service.CategoryService;
import com.zs.assignment11.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/GetallCategories")
    public Map<String, Object> handleGetAllCategories(){
        log.debug("/GetallCategories endpoint was called");
        List<Category> categories = categoryService.getAllCategories();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "all category fetched successfully");
        response.put("categories", categories);
        response.put("totalCategory", categories.size());
        response.put("totalCategoryCount", categories.size());
        return response;
    }

 
    @PostMapping("/addCategory")
    public Map<String, Object> handleAddCategory(@RequestBody Category category){
        log.debug("/addCategory endpoint was called");
        Category addedCategory = categoryService.addCategory(category);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "category added successfully with id: " + addedCategory.getId());
        response.put("addedCategory", addedCategory);
        return response;
    }

    @GetMapping("/{categoryId}/products")
    public Map<String, Object> handleGetAllProductByCategoryId(@PathVariable Long categoryId){
        log.debug("/{categoryId}/products endpoint was called");
        List<Product> products = categoryService.getProductsByCategoryId(categoryId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "products fetched successfully for category id: " + categoryId);
        response.put("products", products);
        response.put("totalProductCountInCategory", products.size());
        return response;
    }

    @DeleteMapping("/deleteCategory")
    public Map<String, Object> handleDeleteCategory(@RequestBody Map<String, Long> body){
        Long categoryId = body.get("categoryId");
        log.debug("/deleteCategory endpoint was called");
        Category deletedCategory = categoryService.deleteCategory(categoryId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "category deleted with id :" + categoryId);
        response.put("deletedCategory", deletedCategory);
        return response;
    }

    @PatchMapping("/updateCategory")
    public Map<String, Object> handleUpdateCategory(@RequestBody Category category){
        log.debug("/updateCategory endpoint was called");
        Category updatedCategory = categoryService.updateCategory(category);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "category updated with " + updatedCategory.getId());
        return response;
    }

}
