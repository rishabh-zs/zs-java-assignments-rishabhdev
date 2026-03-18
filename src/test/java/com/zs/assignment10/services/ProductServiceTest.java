package com.zs.assignment10.services;

import com.zs.assignment10.dao.ProductDao;
import com.zs.assignment10.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * The type Product service test.
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    private ProductService productService;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }


    /**
     * Clean up when dao succeeds returns true.
     *
     * @throws Exception the exception
     */
    @Test
    void cleanUp_WhenDaoSucceeds_ReturnsTrue() throws Exception {
        when(productDao.cleanUp()).thenReturn(true);
        assertTrue(productService.cleanUp());
    }

    /**
     * Clean up when dao throws returns false.
     *
     * @throws Exception the exception
     */
    @Test
    void cleanUp_WhenDaoThrows_ReturnsFalse() throws Exception {
        when(productDao.cleanUp()).thenThrow(new Exception("DB error"));
        assertFalse(productService.cleanUp());
    }


    /**
     * Gets all products returns list.
     *
     * @throws Exception the exception
     */
    @Test
    void getAllProducts_ReturnsList() throws Exception {
        List<Product> products = List.of(
                new Product(1, "Laptop", 1200.0),
                new Product(2, "Mouse", 25.5)
        );
        when(productDao.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals(products, result);
        verify(productDao).findAll();
        verify(productDao, times(1)).findAll();
    }

    /**
     * Gets all products empty table returns empty list.
     *
     * @throws Exception the exception
     */
    @Test
    void getAllProducts_EmptyTable_ReturnsEmptyList() throws Exception {
        when(productDao.findAll()).thenReturn(Collections.emptyList());
        List<Product> result = productService.getAllProducts();

        assertTrue(result.isEmpty());
        assertEquals(Collections.emptyList(), result);
        verify(productDao).findAll();
        verify(productDao, times(1)).findAll();
    }

    /**
     * Gets all products when dao throws runtime exception.
     *
     * @throws Exception the exception
     */
    @Test
    void getAllProducts_WhenDaoThrows_ThrowsRuntimeException() throws Exception {
        when(productDao.findAll()).thenThrow(new Exception("DB error"));
        assertThrows(RuntimeException.class, () -> productService.getAllProducts());
    }


    /**
     * Gets product valid id returns product.
     *
     * @throws Exception the exception
     */
    @Test
    void getProduct_ValidId_ReturnsProduct() throws Exception {
        Product expected = new Product(1, "Laptop", 1200.0);
        when(productDao.findById(1)).thenReturn(expected);

        Product result = productService.getProduct(1);

        assertNotNull(result);
        assertEquals(result, expected);
        assertEquals("Laptop", result.getName());
        verify(productDao, times(1)).findById(1);
    }

    /**
     * Gets product not found returns null.
     *
     * @throws Exception the exception
     */
    @Test
    void getProduct_NotFound_ReturnsNull() throws Exception {
        when(productDao.findById(999)).thenReturn(null);
        assertNull(productService.getProduct(999));
        verify(productDao, times(1)).findById(999);
    }

    /**
     * Gets product invalid id throws illegal argument exception.
     *
     * @param id the id
     */
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    void getProduct_InvalidId_ThrowsIllegalArgumentException(int id) {
        assertThrows(IllegalArgumentException.class, () -> productService.getProduct(id));
        verifyNoInteractions(productDao);
    }

    /**
     * Gets product null id throws illegal argument exception.
     */
    @Test
    void getProduct_NullId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> productService.getProduct(null));
        verifyNoInteractions(productDao);
    }


    /**
     * Delete product valid id returns deleted product.
     *
     * @throws Exception the exception
     */
    @Test
    void deleteProduct_ValidId_ReturnsDeletedProduct() throws Exception {
        Product deleted = new Product(2, "Mouse", 25.5);
        when(productDao.exists(2)).thenReturn(true);
        when(productDao.deleteById(2)).thenReturn(deleted);

        Product result = productService.deleteProduct(2);

        assertNotNull(result);
        assertEquals(result, deleted);
        assertEquals(2, result.getId());
        verify(productDao).deleteById(2);
    }

    /**
     * Delete product invalid id throws illegal argument exception.
     *
     * @param id the id
     */
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -50})
    void deleteProduct_InvalidId_ThrowsIllegalArgumentException(int id) {
        assertThrows(IllegalArgumentException.class, () -> productService.deleteProduct(id));
        verifyNoInteractions(productDao);
    }

    /**
     * Delete product null id throws illegal argument exception.
     */
    @Test
    void deleteProduct_NullId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> productService.deleteProduct(null));
    }

    /**
     * Delete product non-existent id throws illegal argument exception.
     */
    @Test
    void deleteProduct_NonExistentId_ThrowsIllegalArgumentException() {
        when(productDao.exists(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> productService.deleteProduct(999));
    }


    /**
     * Insert product valid product returns inserted.
     *
     * @throws Exception the exception
     */
    @Test
    void insertProduct_ValidProduct_ReturnsInserted() throws Exception {
        Product product = new Product(null, "Keyboard", 75.0);
        when(productDao.insert(product)).thenReturn(product);

        Product result = productService.insertProduct(product);

        assertNotNull(result);
        assertEquals("Keyboard", result.getName());
        verify(productDao).insert(product);
    }

    /**
     * Insert product blank or null name throws illegal argument exception.
     *
     * @param name the name
     */
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void insertProduct_BlankOrNullName_ThrowsIllegalArgumentException(String name) {
        Product product = new Product(null, name, 50.0);
        assertThrows(IllegalArgumentException.class, () -> productService.insertProduct(product));
        verifyNoInteractions(productDao);
    }

    /**
     * Insert product negative price throws illegal argument exception.
     *
     * @param price the price
     */
    @ParameterizedTest
    @ValueSource(doubles = {-0.01, -1.0, -100.0})
    void insertProduct_NegativePrice_ThrowsIllegalArgumentException(double price) {
        Product product = new Product(null, "Item", price);
        assertThrows(IllegalArgumentException.class, () -> productService.insertProduct(product));
        verifyNoInteractions(productDao);
    }

    /**
     * Insert product null price throws illegal argument exception.
     */
    @Test
    void insertProduct_NullPrice_ThrowsIllegalArgumentException() {
        Product product = new Product(null, "Item", null);
        assertThrows(IllegalArgumentException.class, () -> productService.insertProduct(product));
    }

    /**
     * Update product valid product returns updated.
     *
     * @throws Exception the exception
     */
    @Test
    void updateProduct_ValidProduct_ReturnsUpdated() throws Exception {
        Product product = new Product(1, "Gaming Laptop", 1500.0);
        when(productDao.update(product)).thenReturn(product);

        Product result = productService.updateProduct(product);

        assertNotNull(result);
        assertEquals("Gaming Laptop", result.getName());
        verify(productDao).update(product);
    }

    /**
     * Update product invalid id throws illegal argument exception.
     *
     * @param id the id
     */
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -50})
    void updateProduct_InvalidId_ThrowsIllegalArgumentException(int id) {
        Product product = new Product(id, "Item", 10.0);
        assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(product));
        verifyNoInteractions(productDao);
    }

    /**
     * Update product null id throws illegal argument exception.
     */
    @Test
    void updateProduct_NullId_ThrowsIllegalArgumentException() {
        Product product = new Product(null, "Item", 10.0);
        assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(product));
    }

    /**
     * Update product blank or null name throws illegal argument exception.
     *
     * @param name the name
     */
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void updateProduct_BlankOrNullName_ThrowsIllegalArgumentException(String name) {
        Product product = new Product(1, name, 10.0);
        assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(product));
        verifyNoInteractions(productDao);
    }

    /**
     * Update product negative price throws illegal argument exception.
     *
     * @param price the price
     */
    @ParameterizedTest
    @ValueSource(doubles = {-0.01, -1.0, -999.0})
    void updateProduct_NegativePrice_ThrowsIllegalArgumentException(double price) {
        Product product = new Product(1, "Item", price);
        assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(product));
        verifyNoInteractions(productDao);
    }

    /**
     * Update product dao returns null service returns null.
     *
     * @throws Exception the exception
     */
    @Test
    void updateProduct_DaoReturnsNull_ServiceReturnsNull() throws Exception {
        Product product = new Product(1, "Item", 10.0);
        when(productDao.update(product)).thenReturn(null);
        assertNull(productService.updateProduct(product));
    }
}
