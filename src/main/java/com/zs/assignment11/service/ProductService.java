package com.zs.assignment11.service;

import com.zs.assignment11.dao.ProductJpaRepository;
import com.zs.assignment11.exception.ProductAlreadyExistsException;
import com.zs.assignment11.exception.ProductNotFoundException;
import com.zs.assignment11.model.Product;
import com.zs.assignment11.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Product service.
 */
@Service
public class ProductService {
    private static final Logger log = LoggerUtil.getLogger(ProductService.class);
    private final ProductJpaRepository productJpaRepository;

    /**
     * Instantiates a new Product service.
     *
     * @param productJpaRepository the product dao
     */
    public ProductService(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    /**
     * Gets all products.
     *
     * @return the all products
     */
    public List<Product> getAllProducts() {
        log.info("Request received to fetch all products");
        List<Product> products;

        try {
            products = productJpaRepository.findAllByOrderById();
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("Failed to fetch all products", ex);
        }
        return products;
    }

    /**
     * Add product product.
     *
     * @param product the product
     * @return the product
     */
    public Product addProduct(Product product) {
        validateProduct(product);
        log.info("Request received to add product");
        Product addedProduct;

        try {
            product.setId(null);
            addedProduct = productJpaRepository.save(product);
            log.info("Product added successfully: {}", product.getName());
        } catch (DataIntegrityViolationException ex) {
            log.warn("Duplicate product name: {}", product.getName());
            throw new ProductAlreadyExistsException(product.getName());
        } catch (DataAccessException ex) {
            log.error("Database error while adding product: {}", product.getName(), ex);
            throw new RuntimeException("Failed to add product to database.", ex);
        }
        return addedProduct;
    }

    /**
     * Delete product product.
     *
     * @param productId the product id
     * @return the product
     */
    public Product deleteProduct(Integer productId) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product id must be a positive number.");
        }
        log.info("Request received to delete product by id: {}", productId);
        Integer id = Math.toIntExact(productId);
        Product existingProduct = productJpaRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        try {
            productJpaRepository.delete(existingProduct);
            log.info("Deleted product by id: {}", productId);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to delete Product from database.", ex);
        }
        return existingProduct;
    }

    /**
     * Update product product.
     *
     * @param product the product
     * @return the product
     */
    public Product updateProduct(Product product) {
        validateProductForUpdate(product);
        log.info("Request received to update product id: {}", product.getId());
        Product existingProduct = productJpaRepository.findById(product.getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found for id: " + product.getId()));

        Product updatedProduct;
        try {
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            updatedProduct = productJpaRepository.save(existingProduct);
            log.info("Product updated successfully, id: {}", product.getId());
        } catch (DataAccessException ex) {
            log.error("Error while updating product id: {}", product.getId(), ex);
            throw new RuntimeException("Failed to update product in database.", ex);
        }
        return updatedProduct;
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
        if (product.getCategory_id() == null || product.getCategory_id() <= 0) {
            throw new IllegalArgumentException("Category id must be a positive number.");
        }
    }

    private void validateProductForUpdate(Product product) {
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
    }
}

