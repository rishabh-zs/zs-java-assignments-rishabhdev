package com.zs.assignment11.dao;

import com.zs.assignment11.model.Category;
import com.zs.assignment11.model.Product;
import java.util.List;

/**
 * The interface Category dao.
 */
public interface CategoryDao {
    /**
     * Create category table.
     */
    void CreateCategoryTable();

    /**
     * Find all categories list.
     *
     * @return the list
     */
    List<Category> findAllCategories();

    /**
     * Find all products by category id list.
     *
     * @param categoryId the category id
     * @return the list
     */
    List<Product> findAllProductsByCategoryId(Long categoryId);

    /**
     * Add category category.
     *
     * @param category the category
     * @return the category
     */
    Category  addCategory(Category category);

    /**
     * Delete category category.
     *
     * @param id the id
     * @return the category
     */
    Category  deleteCategory(Long id);

    /**
     * Update category category.
     *
     * @param category the category
     * @return the category
     */
    Category  updateCategory(Category category);
}
