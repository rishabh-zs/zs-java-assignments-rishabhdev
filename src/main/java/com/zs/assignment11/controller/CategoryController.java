package com.zs.assignment11.controller;

import com.zs.assignment11.model.Category;
import com.zs.assignment11.model.Product;
import com.zs.assignment11.service.CategoryService;
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
 * The type Category controller.
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private static final Logger log = LoggerUtil.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    /**
     * Instantiates a new Category controller.
     *
     * @param categoryService the category service
     */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Handle get all categories map.
     *
     * @return the map
     */
    @GetMapping("/GetallCategories")
    public Map<String, Object> handleGetAllCategories() {
        log.debug("/GetallCategories endpoint was called");
        List<Category> categories = categoryService.getAllCategories();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "all category fetched successfully");
        response.put("categories", categories);
        response.put("totalCategoryCount", categories.size());
        return response;
    }

    /**
     * Handle add category map.
     *
     * @param category the category
     * @return the map
     */
    @PostMapping("/aCategory")
    public Map<String, Object> handleAddCategory(@RequestBody Category category) {
        log.debug("/addCategory endpoint was called");
        Category addedCategory = categoryService.addCategory(category);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "category added successfully with id: " + addedCategory.getId());
        response.put("addedCategory", addedCategory);
        return response;
    }

    /**
     * Handle get all product by category id map.
     *
     * @param categoryId the category id
     * @return the map
     */
    @GetMapping("/{categoryId}/products")
    public Map<String, Object> handleGetAllProductByCategoryId(@PathVariable Integer categoryId) {
        log.debug("/{categoryId}/products endpoint was called");
        List<Product> products = categoryService.getProductsByCategoryId(categoryId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "products fetched successfully for category id: " + categoryId);
        response.put("products", products);
        response.put("totalProductCountInCategory", products.size());
        return response;
    }

    /**
     * Handle delete category map.
     *
     * @param categoryId the body
     * @return the map
     */
    @DeleteMapping("/dCategory/{categoryId}")
    public Map<String, Object> handleDeleteCategory(@PathVariable Integer categoryId) {
        Integer catId = categoryId;
        log.debug("/deleteCategory endpoint was called");
        Category deletedCategory = categoryService.deleteCategory(catId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "category deleted with id :" + categoryId);
        response.put("deletedCategory", deletedCategory);
        return response;
    }

    /**
     * Handle update category map.
     *
     * @param category the category
     * @return the map
     */
    @PatchMapping("uCategory")
    public Map<String, Object> handleUpdateCategory(@RequestBody Category category) {
        log.debug("/updateCategory endpoint was called");
        Category updatedCategory = categoryService.updateCategory(category);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "category updated with " + updatedCategory.getId());
        return response;
    }

}
