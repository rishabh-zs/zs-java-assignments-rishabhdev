package com.zs.assignment3.repositories;

import com.zs.assignment3.system.Product;
import java.util.List;

/**
 * The interface Product repository.
 */
public interface ProductRepository {
    /**
     * Find all list.
     *
     * @return the list
     */
    List<Product> findAll();

    /**
     * Save.
     *
     * @param product the product
     */
    void save(Product product);

    /**
     * Delete by id boolean.
     *
     * @param id the id
     * @return the boolean
     */
    boolean deleteById(String id);

    /**
     * Find by id product.
     *
     * @param id the id
     * @return the product
     */
    Product findById(String id);
}