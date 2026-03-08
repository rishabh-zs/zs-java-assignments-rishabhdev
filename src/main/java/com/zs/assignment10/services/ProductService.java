package com.zs.assignment10.services;

import com.zs.assignment10.dao.ProductDao;
import com.zs.assignment10.dao.ProductDaoJdbcImpl;
import com.zs.assignment10.model.Product;
import java.util.List;

/**
 * The type Product service.
 */
public class ProductService {

    private final ProductDao productDao;

    /**
     * Instantiates a new Product service.
     *
     * @param productDaoJdbcImpl the product dao jdbc
     */
    public ProductService(ProductDaoJdbcImpl productDaoJdbcImpl) {
        this.productDao = productDaoJdbcImpl;
    }

    /**
     * Clean up boolean.
     *
     * @return the boolean
     */
    public boolean cleanUp() {
        return productDao.cleanUp();
    }

    /**
     * Gets all products.
     *
     * @return the all products
     */
    public List<Product> getAllProducts() {
        return productDao.findAll();
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
        return productDao.findById(id);
    }

    /**
     * Save product product.
     *
     * @param product the product
     * @return the product
     */
    public Product saveProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        if (product.getPrice() == null || product.getPrice() < 0) {
            throw new IllegalArgumentException("Product price must be a positive number.");
        }
        return productDao.save(product);
    }

    /**
     * Delete product boolean.
     *
     * @param id the id
     * @return the boolean
     */
    public boolean deleteProduct(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid Product ID.");
        }
        if (!productDao.exists(id)) {
            throw new IllegalArgumentException("Cannot delete: Product with ID " + id + " does not exist.");
        }
        return productDao.deleteById(id);
    }
}