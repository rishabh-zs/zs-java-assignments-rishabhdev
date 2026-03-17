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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Product controller test.
 */
@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    /** Helper – builds a controller whose Scanner reads from the given lines. */
    private ProductController controllerWith(String simulatedInput) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        return new ProductController(productService, scanner);
    }

    // ─────────────────────────── showMenu ───────────────────────────

    @ParameterizedTest
    @ValueSource(strings = {"0\n", "1\n", "2\n", "3\n", "4\n", "5\n"})
    void showMenu_ValidNumericInput_ReturnsParsedChoice(String input) {
        int expected = Integer.parseInt(input.trim());
        assertEquals(expected, controllerWith(input).showMenu());
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc\n", "\n", "1.5\n", "one\n"})
    void showMenu_NonNumericInput_ReturnsMinusOne(String input) {
        assertEquals(-1, controllerWith(input).showMenu());
    }

    @Test
    void showMenu_InputWithWhitespace_ReturnsTrimmedParsedChoice() {
        assertEquals(4, controllerWith("   4   \n").showMenu());
    }

    // ─────────────────────────── start ──────────────────────────────

    @Test
    void start_WhenCleanupFails_StopsWithoutShowingMenu() {
        ProductController controller = spy(controllerWith(""));
        doReturn(false).when(controller).handleCleanUp();

        controller.start();

        verify(controller, times(1)).handleCleanUp();
        verify(controller, never()).showMenu();
        verify(controller, never()).displayAllProducts();
        verify(controller, never()).handleDisplayProduct();
    }

    @Test
    void start_WhenChoiceIsDisplayThenExit_CallsDisplayAllProductsOnce() {
        ProductController controller = spy(controllerWith(""));
        doReturn(true).when(controller).handleCleanUp();
        doReturn(1, 0).when(controller).showMenu();

        controller.start();

        verify(controller, times(1)).displayAllProducts();
        verify(controller, times(2)).showMenu();
    }

    @Test
    void start_WhenChoiceIsFindByIdThenExit_CallsHandleDisplayProductOnce() {
        ProductController controller = spy(controllerWith(""));
        doReturn(true).when(controller).handleCleanUp();
        doReturn(2, 0).when(controller).showMenu();

        controller.start();

        verify(controller, times(1)).handleDisplayProduct();
    }



    @Test
    void start_WhenChoiceIsInvalidThenExit_DoesNotCallAnyPublicActionHandler() {
        ProductController controller = spy(controllerWith(""));
        doReturn(true).when(controller).handleCleanUp();
        doReturn(-1, 0).when(controller).showMenu();

        controller.start();

        verify(controller, never()).displayAllProducts();
        verify(controller, never()).handleDisplayProduct();
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


}
