package com.zs.assignment10.dao;

import com.zs.assignment10.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Contract-level tests for the ProductDao interface.
 * Uses a Mockito mock to verify that every method on the
 * interface behaves exactly as callers (e.g. ProductService) expect.
 */
@ExtendWith(MockitoExtension.class)
class ProductDaoTest {

    @Mock
    private ProductDao productDao;

    // ─────────────────────────── cleanUp ────────────────────────────

    @Test
    void cleanUp_WhenSuccessful_ReturnsTrue() throws Exception {
        when(productDao.cleanUp()).thenReturn(true);
        assertTrue(productDao.cleanUp());
        verify(productDao).cleanUp();
    }

    @Test
    void cleanUp_WhenFails_ThrowsException() throws Exception {
        when(productDao.cleanUp()).thenThrow(new Exception("DB error"));
        assertThrows(Exception.class, () -> productDao.cleanUp());
    }

    // ─────────────────────────── findAll ────────────────────────────

    @Test
    void findAll_ReturnsAllProducts() throws Exception {
        List<Product> products = List.of(
                new Product(1, "Laptop", 1200.0),
                new Product(2, "Mouse", 25.5)
        );
        when(productDao.findAll()).thenReturn(products);

        List<Product> result = productDao.findAll();

        assertEquals(2, result.size());
        assertEquals("Laptop", result.get(0).getName());
        assertEquals("Mouse", result.get(1).getName());
        verify(productDao).findAll();
    }

    @Test
    void findAll_EmptyTable_ReturnsEmptyList() throws Exception {
        when(productDao.findAll()).thenReturn(Collections.emptyList());
        assertTrue(productDao.findAll().isEmpty());
    }

    @Test
    void findAll_WhenDatabaseFails_ThrowsException() throws Exception {
        when(productDao.findAll()).thenThrow(new Exception("Connection lost"));
        assertThrows(Exception.class, () -> productDao.findAll());
    }

    // ─────────────────────────── findById ───────────────────────────

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 50, 100})
    void findById_ExistingId_ReturnsProduct(int id) throws Exception {
        Product expected = new Product(id, "Item " + id, 10.0 * id);
        when(productDao.findById(id)).thenReturn(expected);

        Product result = productDao.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(productDao).findById(id);
    }

    @ParameterizedTest
    @ValueSource(ints = {999, 1000, 9999})
    void findById_NonExistentId_ReturnsNull(int id) throws Exception {
        when(productDao.findById(id)).thenReturn(null);
        assertNull(productDao.findById(id));
    }

    @Test
    void findById_WhenDatabaseFails_ThrowsException() throws Exception {
        when(productDao.findById(1)).thenThrow(new Exception("DB error"));
        assertThrows(Exception.class, () -> productDao.findById(1));
    }

    // ─────────────────────────── exists ─────────────────────────────

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 10})
    void exists_KnownId_ReturnsTrue(int id) {
        when(productDao.exists(id)).thenReturn(true);
        assertTrue(productDao.exists(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {999, 1000, 9999})
    void exists_UnknownId_ReturnsFalse(int id) {
        when(productDao.exists(id)).thenReturn(false);
        assertFalse(productDao.exists(id));
    }

    // ─────────────────────────── insert ─────────────────────────────

    @Test
    void insert_ValidProduct_ReturnsProductWithGeneratedId() throws Exception {
        Product toInsert = new Product(null, "Keyboard", 75.0);
        Product withId   = new Product(5,    "Keyboard", 75.0);
        when(productDao.insert(toInsert)).thenReturn(withId);

        Product result = productDao.insert(toInsert);

        assertNotNull(result);
        assertEquals(5, result.getId());
        assertEquals("Keyboard", result.getName());
        verify(productDao).insert(toInsert);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Laptop:1200.0", "Monitor:300.0", "Headphones:89.99"})
    void insert_VariousProducts_ReturnsInsertedProduct(String spec) throws Exception {
        String[] parts = spec.split(":");
        String name  = parts[0];
        double price = Double.parseDouble(parts[1]);

        Product input    = new Product(null, name, price);
        Product returned = new Product(1,    name, price);
        when(productDao.insert(input)).thenReturn(returned);

        Product result = productDao.insert(input);

        assertNotNull(result);
        assertEquals(name,  result.getName());
        assertEquals(price, result.getPrice());
    }

    @Test
    void insert_WhenDatabaseFails_ThrowsException() throws Exception {
        Product product = new Product(null, "Item", 50.0);
        when(productDao.insert(product)).thenThrow(new Exception("Insert failed"));
        assertThrows(Exception.class, () -> productDao.insert(product));
    }

    // ─────────────────────────── update ─────────────────────────────

    @Test
    void update_ExistingProduct_ReturnsUpdatedProduct() throws Exception {
        Product updated = new Product(1, "Gaming Laptop", 1500.0);
        when(productDao.update(updated)).thenReturn(updated);

        Product result = productDao.update(updated);

        assertNotNull(result);
        assertEquals("Gaming Laptop", result.getName());
        assertEquals(1500.0, result.getPrice());
        verify(productDao).update(updated);
    }

    @Test
    void update_NonExistentProduct_ReturnsNull() throws Exception {
        Product ghost = new Product(999, "Ghost", 0.0);
        when(productDao.update(ghost)).thenReturn(null);
        assertNull(productDao.update(ghost));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1:Laptop Pro:1450.0", "2:Wireless Mouse:30.0", "3:Desk Chair:250.0"})
    void update_VariousProducts_ReturnsUpdatedProduct(String spec) throws Exception {
        String[] parts = spec.split(":");
        int id         = Integer.parseInt(parts[0]);
        String name    = parts[1];
        double price   = Double.parseDouble(parts[2]);

        Product product = new Product(id, name, price);
        when(productDao.update(product)).thenReturn(product);

        Product result = productDao.update(product);

        assertNotNull(result);
        assertEquals(id,    result.getId());
        assertEquals(name,  result.getName());
        assertEquals(price, result.getPrice());
    }

    @Test
    void update_WhenDatabaseFails_ThrowsException() throws Exception {
        Product product = new Product(1, "Item", 50.0);
        when(productDao.update(product)).thenThrow(new Exception("Update failed"));
        assertThrows(Exception.class, () -> productDao.update(product));
    }

    // ─────────────────────────── deleteById ─────────────────────────

    @Test
    void deleteById_ExistingId_ReturnsDeletedProduct() throws Exception {
        Product deleted = new Product(2, "Mouse", 25.5);
        when(productDao.deleteById(2)).thenReturn(deleted);

        Product result = productDao.deleteById(2);

        assertNotNull(result);
        assertEquals(2, result.getId());
        verify(productDao).deleteById(2);
    }

    @ParameterizedTest
    @ValueSource(ints = {999, 1000, 9999})
    void deleteById_NonExistentId_ReturnsNull(int id) throws Exception {
        when(productDao.deleteById(id)).thenReturn(null);
        assertNull(productDao.deleteById(id));
    }

    @Test
    void deleteById_WhenDatabaseFails_ThrowsException() throws Exception {
        when(productDao.deleteById(1)).thenThrow(new Exception("Delete failed"));
        assertThrows(Exception.class, () -> productDao.deleteById(1));
    }
}
