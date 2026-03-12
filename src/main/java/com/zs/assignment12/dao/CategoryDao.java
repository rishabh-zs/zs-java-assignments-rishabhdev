package com.zs.assignment12.dao;

import com.zs.assignment12.model.Category;
import com.zs.assignment12.model.Product;

import java.util.List;

public interface CategoryDao {
    void CreateCategoryTable();
    List<Category> findAllCategories();
    List<Product> findAllProductsByCategoryId(Long categoryId);
    void  addCategory(Category category);
}
