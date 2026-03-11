package com.zs.assignment11.service;

import com.zs.assignment11.dao.ProductDao;
import com.zs.assignment11.exception.CannotCreateCategoryTableException;
import com.zs.assignment11.exception.CannotCreateProductTableException;
import com.zs.assignment11.model.Product;
import com.zs.assignment11.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service handling Product business logic.
 */
@Service
public class ProductService {
    private List<Product> products=new ArrayList<>();
    private static final Logger log = LoggerUtil.getLogger(ProductService.class);
    private final ProductDao productDao;

    // Constructor Injection
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }


    public void CreateProductTable(){
        log.info("Request received to create product table");
        try {
            productDao.CreateProductTable();
        } catch (DataAccessException ex) {
            throw new CannotCreateProductTableException("Failed to Product category table.", ex);
        }
    }

    public List<Product> getAllProducts() {
        log.info("Request received to fetch all products");
        try{
            products=productDao.findAllProducts();
        }catch (DataAccessException ex) {
            throw new IllegalArgumentException("Failed to fetch all products", ex);
        }
        return products;
    }

    public void deleteProductById(Long productId) {
        log.info("Request received to delete product by id: {}", productId);
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product id must be a positive number.");
        }
        try{
            productDao.deleteProductById(productId);
            log.info("Deleted product by id: {}", productId);
        }catch(DataAccessException ex){
            throw new RuntimeException("Failed to delete Product from database.",ex);
        }
    }

    public void addProduct(Product product) {
        log.info("Request received to add product");
        validateProduct(product);
        try {
            productDao.addProduct(product);
            log.info("Product added successfully: {}", product.getName());
        } catch (DataAccessException ex) {
            log.error("Database error while adding product: {}", product.getName(), ex);
            throw new RuntimeException("Failed to add product to database.", ex);
        }
    }

    private void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product payload is required.");
        }
        if (product.getId() == null || product.getId() <= 0) {
            throw new IllegalArgumentException("Product id must be a positive number.");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name must not be blank.");
        }
        if (product.getPrice() == null || product.getPrice() < 0) {
            throw new IllegalArgumentException("Product price must be zero or greater.");
        }
        if (product.getCategoryId() == null || product.getCategoryId() <= 0) {
            throw new IllegalArgumentException("Category id must be a positive number.");
        }
    }
}
