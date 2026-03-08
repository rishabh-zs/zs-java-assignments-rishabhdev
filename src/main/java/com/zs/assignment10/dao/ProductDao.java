package com.zs.assignment10.dao;

import com.zs.assignment10.model.Product;
import java.util.List;

/**
 * The interface Product dao.
 */
public interface ProductDao {
    /**
     * Clean up boolean.
     *
     * @return the boolean
     */
    boolean cleanUp();

    /**
     * Find all list.
     *
     * @return the list
     */
    List<Product> findAll();

    /**
     * Find by id product.
     *
     * @param id the id
     * @return the product
     */
    Product findById(Integer id);

    /**
     * Save product.
     *
     * @param product the product
     * @return the product
     */
    Product save(Product product);

    /**
     * Delete by id boolean.
     *
     * @param id the id
     * @return the boolean
     */
    boolean deleteById(Integer id);

    /**
     * Exists boolean.
     *
     * @param id the id
     * @return the boolean
     */
    boolean exists(Integer id);
}