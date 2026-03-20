package com.zs.assignment11.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The type Global exception handler.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle product already exists response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleProductAlreadyExists(ProductAlreadyExistsException ex) {
        return buildError(HttpStatus.CONFLICT, "Products already exists");
    }

    /**
     * Handle category already exists response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleCategoryAlreadyExists(CategoryAlreadyExistsException ex) {
        return buildError(HttpStatus.CONFLICT, "Categories already exists");
    }

    /**
     * Handle product not found response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProductNotFound(ProductNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, "product id does not exists");
    }

    /**
     * Handle category not found response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCategoryNotFound(CategoryNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, "category id does not exists");
    }

    /**
     * Handle illegal argument response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        String message = ex.getMessage();
        if (message != null && message.toLowerCase().contains("product id")) {
            return buildError(HttpStatus.NOT_FOUND, "product id does not exists");
        }
        if (message != null && message.toLowerCase().contains("category id")) {
            return buildError(HttpStatus.NOT_FOUND, "category id does not exists");
        }
        return buildError(HttpStatus.BAD_REQUEST, message != null ? message : "invalid request");
    }

    private ResponseEntity<Map<String, Object>> buildError(HttpStatus statusCode, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "error");
        response.put("message", message);
        return ResponseEntity.status(statusCode).body(response);
    }
}

