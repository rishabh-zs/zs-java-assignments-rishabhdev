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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    // ─────────────────────────── cleanUp ────────────────────────────

    @Test
    void cleanUp_WhenDaoSucceeds_ReturnsTrue() throws Exception {
        when(productDao.cleanUp()).thenReturn(true);
        assertTrue(productService.cleanUp());
    }

    @Test
    void cleanUp_WhenDaoThrows_ReturnsFalse() throws Exception {
        when(productDao.cleanUp()).thenThrow(new Exception("DB error"));
        assertFalse(productService.cleanUp());
    }

    // ─────────────────────────── getAllProducts ──────────────────────

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

    @Test
    void getAllProducts_EmptyTable_ReturnsEmptyList() throws Exception {
        when(productDao.findAll()).thenReturn(Collections.emptyList());
        List<Product> result = productService.getAllProducts();

        assertTrue(result.isEmpty());
        assertEquals(Collections.emptyList(), result);
        verify(productDao).findAll();
        verify(productDao, times(1)).findAll();
    }

    @Test
    void getAllProducts_WhenDaoThrows_ThrowsRuntimeException() throws Exception {
        when(productDao.findAll()).thenThrow(new Exception("DB error"));
        assertThrows(RuntimeException.class, () -> productService.getAllProducts());
    }

    // ─────────────────────────── getProduct ─────────────────────────

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

    @Test
    void getProduct_NotFound_ReturnsNull() throws Exception {
        when(productDao.findById(999)).thenReturn(null);
        assertNull(productService.getProduct(999));
        verify(productDao, times(1)).findById(999);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    void getProduct_InvalidId_ThrowsIllegalArgumentException(int id) {
        assertThrows(IllegalArgumentException.class, () -> productService.getProduct(id));
        verifyNoInteractions(productDao);
    }

    @Test
    void getProduct_NullId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> productService.getProduct(null));
        verifyNoInteractions(productDao);
    }

    // ─────────────────────────── deleteProduct ──────────────────────

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

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -50})
    void deleteProduct_InvalidId_ThrowsIllegalArgumentException(int id) {
        assertThrows(IllegalArgumentException.class, () -> productService.deleteProduct(id));
        verifyNoInteractions(productDao);
    }

    @Test
    void deleteProduct_NullId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> productService.deleteProduct(null));
    }

    @Test
    void deleteProduct_NonExistentId_ThrowsIllegalArgumentException() {
        when(productDao.exists(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> productService.deleteProduct(999));
    }

    // ─────────────────────────── insertProduct ──────────────────────

    @Test
    void insertProduct_ValidProduct_ReturnsInserted() throws Exception {
        Product product = new Product(null, "Keyboard", 75.0);
        when(productDao.insert(product)).thenReturn(product);

        Product result = productService.insertProduct(product);

        assertNotNull(result);
        assertEquals("Keyboard", result.getName());
        verify(productDao).insert(product);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void insertProduct_BlankOrNullName_ThrowsIllegalArgumentException(String name) {
        Product product = new Product(null, name, 50.0);
        assertThrows(IllegalArgumentException.class, () -> productService.insertProduct(product));
        verifyNoInteractions(productDao);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.01, -1.0, -100.0})
    void insertProduct_NegativePrice_ThrowsIllegalArgumentException(double price) {
        Product product = new Product(null, "Item", price);
        assertThrows(IllegalArgumentException.class, () -> productService.insertProduct(product));
        verifyNoInteractions(productDao);
    }

    @Test
    void insertProduct_NullPrice_ThrowsIllegalArgumentException() {
        Product product = new Product(null, "Item", null);
        assertThrows(IllegalArgumentException.class, () -> productService.insertProduct(product));
    }

    // ─────────────────────────── updateProduct ──────────────────────

    @Test
    void updateProduct_ValidProduct_ReturnsUpdated() throws Exception {
        Product product = new Product(1, "Gaming Laptop", 1500.0);
        when(productDao.update(product)).thenReturn(product);

        Product result = productService.updateProduct(product);

        assertNotNull(result);
        assertEquals("Gaming Laptop", result.getName());
        verify(productDao).update(product);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -50})
    void updateProduct_InvalidId_ThrowsIllegalArgumentException(int id) {
        Product product = new Product(id, "Item", 10.0);
        assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(product));
        verifyNoInteractions(productDao);
    }

    @Test
    void updateProduct_NullId_ThrowsIllegalArgumentException() {
        Product product = new Product(null, "Item", 10.0);
        assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(product));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void updateProduct_BlankOrNullName_ThrowsIllegalArgumentException(String name) {
        Product product = new Product(1, name, 10.0);
        assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(product));
        verifyNoInteractions(productDao);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.01, -1.0, -999.0})
    void updateProduct_NegativePrice_ThrowsIllegalArgumentException(double price) {
        Product product = new Product(1, "Item", price);
        assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(product));
        verifyNoInteractions(productDao);
    }

    @Test
    void updateProduct_DaoReturnsNull_ServiceReturnsNull() throws Exception {
        Product product = new Product(1, "Item", 10.0);
        when(productDao.update(product)).thenReturn(null);
        assertNull(productService.updateProduct(product));
    }
}
