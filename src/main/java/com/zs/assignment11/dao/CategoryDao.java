package com.zs.assignment11.dao;

import com.zs.assignment11.model.Category;
import com.zs.assignment11.model.Product;

import java.util.List;

public interface CategoryDao {
    void CreateCategoryTable();
    List<Category> findAllCategories();
    List<Product> findAllProductsByCategoryId(Long categoryId);
}
