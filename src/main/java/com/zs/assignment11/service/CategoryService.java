package com.zs.assignment11.service;

import com.zs.assignment11.dao.CategoryDao;
import com.zs.assignment11.exception.CannotCreateCategoryTableException;
import com.zs.assignment11.exception.CannotGetAllCategoryException;
import com.zs.assignment11.exception.CannotGetAllProductByCategoryIdException;
import com.zs.assignment11.exception.CategoryAlreadyExistsException;
import com.zs.assignment11.model.Category;
import com.zs.assignment11.model.Product;
import com.zs.assignment11.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CategoryService {
    private List<Category> categories=new ArrayList<>();
    private List<Product> products=new ArrayList<>();
    private static final Logger log = LoggerUtil.getLogger(CategoryService.class);
    private final CategoryDao categoryDao;

    // Constructor Injection
    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public void CreateCategoryTable(){
        log.info("Request received to create category table");
        try {
            categoryDao.CreateCategoryTable();
        } catch (DataAccessException ex) {
            throw new CannotCreateCategoryTableException("Failed to create category table.", ex);
        }
    }

    public List<Category> getAllCategories() {
        log.info("Request received to fetch all categories");
        try{
            categories=categoryDao.findAllCategories();
        }catch(DataRetrievalFailureException ex){
            throw new CannotGetAllCategoryException("Failed to fetch all categories.", ex);
        }
        return categories;
    }

    public List<Product> getProductsByCategoryId(Long categoryId) {
        log.info("Request received to fetch products for category id: {}", categoryId);
        if (categoryId == null || categoryId <= 0) {
            throw new IllegalArgumentException("Category id must be a positive number.");
        }
        try{
            products=categoryDao.findAllProductsByCategoryId(categoryId);
            return products;
        }catch(DataAccessException ex){
            throw new CannotGetAllProductByCategoryIdException("Failed to fetch all products for category id.", ex);
        }
    }

    public void addCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category payload is required.");
        }

        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name must not be blank.");
        }

        log.info("Request received to add category: {}", category.getName());

        try {
            categoryDao.addCategory(category);
            log.info("Category added successfully {}", category.getName());
        } catch (DuplicateKeyException e) {
            log.warn("Duplicate category name: {}", category.getName());
            throw new CategoryAlreadyExistsException("Category already exists");
        } catch (DataAccessException e) {
            log.error("Error while adding category: {}", category.getName(), e);
            throw new RuntimeException("Failed to add category to database.", e);
        }
    }

    public void deleteCategory(Long categoryId) {
        if(categoryId==null || categoryId<=0){
            throw new IllegalArgumentException("Category id must be a positive number.");
        }
        log.info("Request received to delete category id: {}", categoryId);

        try{
            categoryDao.deleteCategory(categoryId);
            log.info("Deleted category id: {}", categoryId);
        }catch(DataAccessException ex){
            log.error("Error while deleting category id: {}", categoryId, ex);
            throw new RuntimeException("Failed to delete category from database.", ex);
        }
    }
}
