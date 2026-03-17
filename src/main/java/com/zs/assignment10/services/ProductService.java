package com.zs.assignment10.services;

import com.zs.assignment10.dao.ProductDao;
import com.zs.assignment10.model.Product;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Product service.
 */
@Slf4j
public class ProductService {

    private final ProductDao productDao;

    /**
     * Instantiates a new Product service.
     *
     * @param productDao the product dao implementation
     */
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * Ensures the products table exists.
     *
     * @return the boolean
     */
    public boolean cleanUp() {
        try {
            if (productDao.cleanUp()) {
                return true;
            }
        } catch (Exception e) {
            logger.error("Failed to prepare products table", e);
        }
        return false;
    }

    /**
     * Gets all products.
     *
     * @return the all products
     */
    public List<Product> getAllProducts() {
        try {
            return productDao.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch products.", e);
        }
    }

    /**
     * Gets product.
     *
     * @param id the id
     * @return the product
     */
    public Product getProduct(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid Product ID.");
        }
        try {
            Product product = productDao.findById(id);
            if (product != null) {
                return product;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Delete product product.
     *
     * @param id the id
     * @return the product
     */
    public Product deleteProduct(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid Product ID.");
        }
        if (!productDao.exists(id)) {
            throw new IllegalArgumentException("Cannot delete: Product with ID " + id + " does not exist.");
        }
        try {
            Product product = productDao.deleteById(id);
            if (product != null) {
                return product;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Insert product product.
     *
     * @param product the product
     * @return the product
     */
    public Product insertProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        if (product.getPrice() == null || product.getPrice() < 0) {
            throw new IllegalArgumentException("Product price must be a positive number.");
        }
        try {
            if (productDao.insert(product) != null) {
                return product;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Update product product.
     *
     * @param product the product
     * @return the product
     */
    public Product updateProduct(Product product) {
        if (product.getId() == null || product.getId() <= 0) {
            throw new IllegalArgumentException("Invalid Product ID.");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        if (product.getPrice() == null || product.getPrice() < 0) {
            throw new IllegalArgumentException("Product price must be a positive number.");
        }
        try {
            if (productDao.update(product) != null) {
                return product;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}