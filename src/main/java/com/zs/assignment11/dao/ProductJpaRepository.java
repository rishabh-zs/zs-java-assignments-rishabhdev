package com.zs.assignment11.dao;

import com.zs.assignment11.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for Product.
 */
public interface ProductJpaRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByOrderById();

    @Query("SELECT p FROM Product p WHERE p.category_id = :categoryId ORDER BY p.id")
    List<Product> findAllByCategoryIdOrderById(@Param("categoryId") Integer categoryId);
}