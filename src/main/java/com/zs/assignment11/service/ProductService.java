package com.zs.assignment11.service;

import com.zs.assignment11.dao.ProductDao;
import com.zs.assignment11.exception.CannotCreateProductTableException;
import com.zs.assignment11.exception.ProductAlreadyExistsException;
import com.zs.assignment11.model.Product;
import com.zs.assignment11.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service handling Product business logic.
 */
@Service
public class ProductService {
    private List<Product> products = new ArrayList<>();
    private static final Logger log = LoggerUtil.getLogger(ProductService.class);
    private final ProductDao productDao;

    // Constructor Injection
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }


    public void CreateProductTable() {
        log.info("Request received to create product table");
        try {
            productDao.CreateProductTable();
        } catch (DataAccessException ex) {
            throw new CannotCreateProductTableException("Failed to Create Product table.", ex);
        }
    }

    public List<Product> getAllProducts() {
        log.info("Request received to fetch all products");
        try {
            products = productDao.findAllProducts();
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("Failed to fetch all products", ex);
        }
        return products;
    }

    public Product addProduct(Product product) {
        log.info("Request received to add product");
        validateProduct(product);
        try {
            Product addedProduct = productDao.addProduct(product);
            log.info("Product added successfully: {}", product.getName());
            return addedProduct;
        } catch (DuplicateKeyException ex) {
            log.warn("Duplicate product name: {}", product.getName());
            throw new ProductAlreadyExistsException(product.getName());
        } catch (DataAccessException ex) {
            log.error("Database error while adding product: {}", product.getName(), ex);
            throw new RuntimeException("Failed to add product to database.", ex);
        }
    }

    public Product deleteProduct(Long productId) {
        log.info("Request received to delete product by id: {}", productId);
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product id must be a positive number.");
        }
        try {
            Product deletedProduct = productDao.deleteProduct(productId);
            log.info("Deleted product by id: {}", productId);
            return deletedProduct;
        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to delete Product from database.", ex);
        }
    }

    public Product updateProduct(Product product) {
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
        log.info("Request received to update product id: {}", product.getId());

        try {
            Product updatedProduct = productDao.updateProduct(product);
            log.info("Product updated successfully, id: {}", product.getId());
            return updatedProduct;
        } catch (DataAccessException ex) {
            log.error("Error while updating product id: {}", product.getId(), ex);
            throw new RuntimeException("Failed to update product in database.", ex);
        }
    }

    private void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product payload is required.");
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

