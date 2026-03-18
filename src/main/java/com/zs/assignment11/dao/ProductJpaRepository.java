package com.zs.assignment11.dao;

import com.zs.assignment11.exception.ProductNotFoundException;
import com.zs.assignment11.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for Product.
 */
public interface ProductJpaRepository extends JpaRepository<Product, Integer>, ProductDao {
    /**
     * Find all order by id list.
     *
     * @return the list
     */
    @Query("SELECT p FROM Product p ORDER BY p.id")
    List<Product> findAllOrderById();

    /**
     * Find all by category id order by id list.
     *
     * @param categoryId the category id
     * @return the list
     */
    @Query("SELECT p FROM Product p WHERE p.category_id = :categoryId ORDER BY p.id")
    List<Product> findAllByCategoryIdOrderById(@Param("categoryId") Integer categoryId);

    /**
     * Hibernate manages schema; this call forces repository initialization.
     */
    @Override
    default void CreateProductTable() {
        count();
    }

    @Override
    default List<Product> findAllProducts() {
        return findAllOrderById();
    }

    @Override
    default Product addProduct(Product product) {
        product.setId(null);
        return save(product);
    }

    @Override
    default Product deleteProduct(Long id) {
        Integer productId = Math.toIntExact(id);
        Product existingProduct = findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found for id: " + id));
        delete(existingProduct);
        return existingProduct;
    }

    @Override
    default Product updateProduct(Product product) {
        Product existingProduct = findById(product.getId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found for id: " + product.getId()));
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        return save(existingProduct);
    }
}

