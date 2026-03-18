package com.zs.assignment11.Dao;

import com.zs.assignment11.dao.ProductJpaRepository;
import com.zs.assignment11.exception.ProductNotFoundException;
import com.zs.assignment11.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Product jpa repository test.
 */
class ProductJpaRepositoryTest {

    private ProductJpaRepository productJpaRepository;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        productJpaRepository = mock(ProductJpaRepository.class, Answers.CALLS_REAL_METHODS);
    }

    /**
     * Create product table delegates to count.
     */
    @Test
    void createProductTable_DelegatesToCount() {
        when(productJpaRepository.count()).thenReturn(1L);

        productJpaRepository.CreateProductTable();

        verify(productJpaRepository).count();
    }

    /**
     * Find all products returns ordered products.
     */
    @Test
    void findAllProducts_ReturnsOrderedProducts() {
        List<Product> products = List.of(
                new Product(1, "laptop", 1000.0, 1),
                new Product(2, "tv", 2000.0, 1)
        );
        when(productJpaRepository.findAllOrderById()).thenReturn(products);

        List<Product> result = productJpaRepository.findAllProducts();

        assertEquals(2, result.size());
        assertEquals("laptop", result.getFirst().getName());
        verify(productJpaRepository).findAllOrderById();
    }

    /**
     * Add product sets null id and saves.
     */
    @Test
    void addProduct_SetsNullIdAndSaves() {
        Product request = new Product(10, "Phone", 999.99, 1);
        Product saved = new Product(1, "Phone", 999.99, 1);
        when(productJpaRepository.save(any(Product.class))).thenReturn(saved);

        Product result = productJpaRepository.addProduct(request);

        assertNull(request.getId());
        assertSame(saved, result);
        verify(productJpaRepository).save(request);
    }

    /**
     * Delete product deletes and returns entity when found.
     */
    @Test
    void deleteProduct_DeletesAndReturnsEntityWhenFound() {
        Product existing = new Product(1, "Phone", 999.99, 1);
        when(productJpaRepository.findById(1)).thenReturn(Optional.of(existing));

        Product result = productJpaRepository.deleteProduct(1L);

        assertSame(existing, result);
        verify(productJpaRepository).findById(1);
        verify(productJpaRepository).delete(existing);
    }

    /**
     * Delete product throws when not found.
     */
    @Test
    void deleteProduct_ThrowsWhenNotFound() {
        when(productJpaRepository.findById(7)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productJpaRepository.deleteProduct(7L)
        );

        assertEquals("Product not found for id: 7", exception.getMessage());
        verify(productJpaRepository).findById(7);
        verify(productJpaRepository, never()).delete(any(Product.class));
    }

    /**
     * Update product updates mutable fields and saves.
     */
    @Test
    void updateProduct_UpdatesMutableFieldsAndSaves() {
        Product existing = new Product(2, "Old", 100.0, 3);
        Product request = new Product(2, "New", 150.0, 99);
        when(productJpaRepository.findById(2)).thenReturn(Optional.of(existing));
        when(productJpaRepository.save(existing)).thenReturn(existing);

        Product result = productJpaRepository.updateProduct(request);

        assertEquals("New", existing.getName());
        assertEquals(150.0, existing.getPrice());
        assertEquals(3, existing.getCategory_id());
        assertSame(existing, result);
        verify(productJpaRepository).findById(2);
        verify(productJpaRepository).save(existing);
    }

    /**
     * Update product throws when not found.
     */
    @Test
    void updateProduct_ThrowsWhenNotFound() {
        Product request = new Product(42, "New", 150.0, 1);
        when(productJpaRepository.findById(42)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productJpaRepository.updateProduct(request)
        );

        assertEquals("Product not found for id: 42", exception.getMessage());
        verify(productJpaRepository).findById(42);
        verify(productJpaRepository, never()).save(any(Product.class));
    }
}

