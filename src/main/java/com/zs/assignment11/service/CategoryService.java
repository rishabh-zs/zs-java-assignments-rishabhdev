package com.zs.assignment11.service;


import com.zs.assignment11.dao.CategoryJpaRepository;
import com.zs.assignment11.exception.CannotGetAllCategoryException;
import com.zs.assignment11.exception.CannotGetAllProductByCategoryIdException;
import com.zs.assignment11.exception.CategoryAlreadyExistsException;
import com.zs.assignment11.exception.CategoryNotFoundException;
import com.zs.assignment11.model.Category;
import com.zs.assignment11.model.Product;
import com.zs.assignment11.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * The type Category service.
 */
@Service
public class CategoryService {
    private static final Logger log = LoggerUtil.getLogger(CategoryService.class);
    private final CategoryJpaRepository categoryJpaRepository;

    /**
     * Instantiates a new Category service.
     *
     * @param categoryJpaRepository the category dao
     */
    public CategoryService(CategoryJpaRepository categoryJpaRepository) {
        this.categoryJpaRepository = categoryJpaRepository;
    }

    /**
     * Gets all categories.
     *
     * @return the all categories
     */
    public List<Category> getAllCategories() {
        log.info("Request received to fetch all categories");
        List<Category> categories;
        try {
            categories = categoryJpaRepository.findAllByOrderById();
        } catch (DataRetrievalFailureException ex) {
            throw new CannotGetAllCategoryException("Failed to fetch all categories.", ex);
        }
        return categories;
    }

    /**
     * Add category category.
     *
     * @param category the category
     * @return the category
     */
    public Category addCategory(Category category) {
        validateCategoryPayload(category);

        log.info("Request received to add category: {}", category.getName());
        Category newCat;
        try {
            category.setId(null);
            newCat = categoryJpaRepository.save(category);
            log.info("Category added successfully: {}", newCat.getName());
        } catch (DataIntegrityViolationException ex) {
            log.warn("Duplicate category name: {}", category.getName());
            throw new CategoryAlreadyExistsException("Category already exists", ex);
        }
        return newCat;
    }

    /**
     * Gets products by category id.
     *
     * @param categoryId the category id
     * @return the products by category id
     */
    public List<Product> getProductsByCategoryId(Integer categoryId) {
        validateCategoryId(categoryId);
        log.info("Request received to fetch products for category id: {}", categoryId);
        Integer id = Math.toIntExact(categoryId);

        if (!categoryJpaRepository.existsById(id)) {
            throw new CategoryNotFoundException("Category not found for id: " + categoryId);
        }
        List<Product> products;
        try {
            products = categoryJpaRepository.findAllProductsByCategoryIdOrderById(categoryId);
        } catch (DataAccessException ex) {
            throw new CannotGetAllProductByCategoryIdException("Failed to fetch all products for category id.", ex);
        }
        return products;
    }


    /**
     * Delete category.
     *
     * @param categoryId the category id
     * @return the category
     */
    public Category deleteCategory(Integer categoryId) {
        validateCategoryId(categoryId);
        log.info("Request received to delete category id: {}", categoryId);
        Integer id = Math.toIntExact(categoryId);

        Category existingCategory = categoryJpaRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found for id: " + categoryId));
        try {
            categoryJpaRepository.delete(existingCategory);
            log.info("Deleted category id: {}", categoryId);
        } catch (DataAccessException ex) {
            log.error("Error while deleting category id: {}", categoryId, ex);
            throw new RuntimeException("Failed to delete category from database.", ex);
        }
        return existingCategory;
    }

    /**
     * Update category.
     *
     * @param category the category
     * @return the category
     */
    public Category updateCategory(Category category) {
        validateCategoryPayload(category);
        if (category.getId() == null || category.getId() <= 0) {
            throw new IllegalArgumentException("Category id must be a positive number.");
        }
        log.info("Request received to update category id: {}", category.getId());

        Category existingCategory = categoryJpaRepository.findById(category.getId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found for id: " + category.getId()));

        Category updatedCategory;
        try {
            existingCategory.setName(category.getName());
            updatedCategory = categoryJpaRepository.save(existingCategory);
            log.info("Category updated successfully, id: {}", category.getId());
        } catch (DataAccessException ex) {
            log.error("Error while updating category id: {}", category.getId(), ex);
            throw new RuntimeException("Failed to update category in database.", ex);
        }
        return updatedCategory;
    }

    private void validateCategoryPayload(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category payload is required.");
        }
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name must not be blank.");
        }
    }

    private void validateCategoryId(Integer categoryId) {
        if (categoryId == null || categoryId <= 0) {
            throw new IllegalArgumentException("Category id must be a positive number.");
        }
    }
}
