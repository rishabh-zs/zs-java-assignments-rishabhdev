package com.zs.assignment12.exception;

import com.zs.assignment12.controller.CategoryController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for Category-related errors.
 */
@RestControllerAdvice(assignableTypes = CategoryController.class)
public class CategoryExceptionHandler {

}
