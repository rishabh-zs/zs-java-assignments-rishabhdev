package com.zs.assignment10.controllers;

import com.zs.assignment10.model.Product;
import com.zs.assignment10.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * The type Product controller test.
 */
@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    /** Helper – builds a controller whose Scanner reads from the given lines. */
    private ProductController controllerWith(String simulatedInput) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        return new ProductController(productService, scanner);
    }

    // ─────────────────────────── handleCleanUp ──────────────────────

    /**
     * Handle clean up when service succeeds returns true.
     */
    @Test
    void handleCleanUp_WhenServiceSucceeds_ReturnsTrue() {
        when(productService.cleanUp()).thenReturn(true);
        assertTrue(controllerWith("").handleCleanUp());
    }

    /**
     * Handle clean up when service fails returns false.
     */
    @Test
    void handleCleanUp_WhenServiceFails_ReturnsFalse() {
        when(productService.cleanUp()).thenReturn(false);
        assertFalse(controllerWith("").handleCleanUp());
    }

    // ─────────────────────────── displayAllProducts ─────────────────

    /**
     * Display all products with products logs products.
     */
    @Test
    void displayAllProducts_WithProducts_LogsProducts() {
        when(productService.getAllProducts()).thenReturn(List.of(
                new Product(1, "Laptop", 1200.0),
                new Product(2, "Mouse", 25.5)
        ));
        // No exception expected; service interaction verified
        controllerWith("").displayAllProducts();
        verify(productService,times(1)).getAllProducts();
    }

    /**
     * Display all products empty list logs no products.
     */
    @Test
    void displayAllProducts_EmptyList_LogsNoProducts() {
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());
        controllerWith("").displayAllProducts();
        verify(productService,times(1)).getAllProducts();
    }

    // ─────────────────────────── handleDisplayProduct ───────────────

    /**
     * Handle display product found calls get product.
     *
     * @param input the input
     */
    @ParameterizedTest
    @ValueSource(strings = {"1\n", "2\n", "10\n"})
    void handleDisplayProduct_ProductFound_CallsGetProduct(String input) {
        int id = Integer.parseInt(input.trim());
        when(productService.getProduct(id)).thenReturn(new Product(id, "Item", 99.0));

        controllerWith(input).handleDisplayProduct();

        verify(productService,times(1)).getProduct(id);
    }

    /**
     * Handle display product not found does not throw.
     */
    @Test
    void handleDisplayProduct_ProductNotFound_DoesNotThrow() {
        when(productService.getProduct(5)).thenReturn(null);
        assertDoesNotThrow(() -> controllerWith("5\n").handleDisplayProduct());
    }

    // ─────────────────────────── handleInsertProduct ────────────────

    /**
     * Handle insert product valid input calls insert product.
     */
    @Test
    void handleInsertProduct_ValidInput_CallsInsertProduct() {
        Product inserted = new Product(3, "Keyboard", 75.0);
        when(productService.insertProduct(any(Product.class))).thenReturn(inserted);

        controllerWith("Keyboard\n75.0\n").handleInsertProduct();

        verify(productService).insertProduct(argThat(p ->
                "Keyboard".equals(p.getName()) && p.getPrice() == 75.0 && p.getId() == null));
    }

    /**
     * Handle insert product service returns null does not throw.
     */
    @Test
    void handleInsertProduct_ServiceReturnsNull_DoesNotThrow() {
        when(productService.insertProduct(any(Product.class))).thenReturn(null);
        assertDoesNotThrow(() -> controllerWith("Monitor\n300.0\n").handleInsertProduct());
    }

    /**
     * Handle insert product various valid inputs calls service once.
     *
     * @param input the input
     */
    @ParameterizedTest
    @ValueSource(strings = {"Laptop\n1200.0\n", "Mouse\n25.5\n", "Headset\n89.99\n"})
    void handleInsertProduct_VariousValidInputs_CallsServiceOnce(String input) {
        when(productService.insertProduct(any())).thenReturn(new Product(1, "x", 1.0));
        controllerWith(input).handleInsertProduct();
        verify(productService, times(1)).insertProduct(any());
    }

    // ─────────────────────────── handleUpdateProduct ────────────────

    /**
     * Handle update product valid input calls update product.
     */
    @Test
    void handleUpdateProduct_ValidInput_CallsUpdateProduct() {
        Product updated = new Product(1, "Gaming Laptop", 1500.0);
        when(productService.updateProduct(any(Product.class))).thenReturn(updated);

        controllerWith("1\nGaming Laptop\n1500.0\n").handleUpdateProduct();

        verify(productService).updateProduct(argThat(p ->
                p.getId() == 1 && "Gaming Laptop".equals(p.getName()) && p.getPrice() == 1500.0));
    }

    /**
     * Handle update product service returns null does not throw.
     */
    @Test
    void handleUpdateProduct_ServiceReturnsNull_DoesNotThrow() {
        when(productService.updateProduct(any(Product.class))).thenReturn(null);
        assertDoesNotThrow(() -> controllerWith("1\nOld Name\n10.0\n").handleUpdateProduct());
    }

    /**
     * Handle update product various valid inputs calls service once.
     *
     * @param input the input
     */
    @ParameterizedTest
    @ValueSource(strings = {"1\nLaptop Pro\n1450.0\n", "2\nWireless Mouse\n30.0\n"})
    void handleUpdateProduct_VariousValidInputs_CallsServiceOnce(String input) {
        when(productService.updateProduct(any())).thenReturn(new Product(1, "x", 1.0));
        controllerWith(input).handleUpdateProduct();
        verify(productService, times(1)).updateProduct(any());
    }

    // ─────────────────────────── handleDeleteProduct ────────────────

    /**
     * Handle delete product product exists calls delete product.
     */
    @Test
    void handleDeleteProduct_ProductExists_CallsDeleteProduct() {
        Product deleted = new Product(2, "Mouse", 25.5);
        when(productService.deleteProduct(2)).thenReturn(deleted);

        controllerWith("2\n").handleDeleteProduct();

        verify(productService).deleteProduct(2);
    }

    /**
     * Handle delete product service returns null does not throw.
     */
    @Test
    void handleDeleteProduct_ServiceReturnsNull_DoesNotThrow() {
        when(productService.deleteProduct(99)).thenReturn(null);
        assertDoesNotThrow(() -> controllerWith("99\n").handleDeleteProduct());
    }

    /**
     * Handle delete product various ids calls service once.
     *
     * @param input the input
     */
    @ParameterizedTest
    @ValueSource(strings = {"1\n", "3\n", "7\n"})
    void handleDeleteProduct_VariousIds_CallsServiceOnce(String input) {
        int id = Integer.parseInt(input.trim());
        when(productService.deleteProduct(id)).thenReturn(new Product(id, "x", 1.0));
        controllerWith(input).handleDeleteProduct();
        verify(productService, times(1)).deleteProduct(id);
    }
}
