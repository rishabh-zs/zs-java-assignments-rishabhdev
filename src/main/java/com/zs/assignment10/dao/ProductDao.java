package com.zs.assignment10.dao;

import com.zs.assignment10.model.Product;

import java.util.List;

/**
 * The interface Product dao.
 */
public interface ProductDao {
    /**
     * Ensures the products table exists.
     *
     * @return the boolean
     * @throws Exception the exception
     */
    boolean cleanUp() throws Exception;

    /**
     * Find all list.
     *
     * @return the list
     * @throws Exception the exception
     */
    List<Product> findAll() throws Exception;

    /**
     * Find by id product.
     *
     * @param id the id
     * @return the product
     * @throws Exception the exception
     */
    Product findById(Integer id) throws Exception;

    /**
     * Delete by id boolean.
     *
     * @param id the id
     * @return the boolean
     * @throws Exception the exception
     */
    Product deleteById(Integer id) throws Exception;

    /**
     * Exists boolean.
     *
     * @param id the id
     * @return the boolean
     */
    boolean exists(Integer id);

    /**
     * Insert product.
     *
     * @param product the product
     * @return the product
     * @throws Exception the exception
     */
    Product insert(Product product) throws Exception;

    /**
     * Update product.
     *
     * @param product the product
     * @return the product
     * @throws Exception the exception
     */
    Product update(Product product) throws Exception;
}