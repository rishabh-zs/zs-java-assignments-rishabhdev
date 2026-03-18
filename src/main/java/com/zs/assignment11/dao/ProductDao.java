package com.zs.assignment11.dao;

import com.zs.assignment11.model.Product;

import java.util.List;

/**
 * The interface Product dao.
 */
public interface ProductDao {
    /**
     * Create product table.
     */
    void CreateProductTable();

    /**
     * Find all products list.
     *
     * @return the list
     */
    List<Product> findAllProducts();

    /**
     * Add product product.
     *
     * @param product the product
     * @return the product
     */
    Product addProduct(Product product);

    /**
     * Delete product product.
     *
     * @param id the id
     * @return the product
     */
    Product deleteProduct(Long id);

    /**
     * Update product product.
     *
     * @param product the product
     * @return the product
     */
    Product updateProduct(Product product);
}
