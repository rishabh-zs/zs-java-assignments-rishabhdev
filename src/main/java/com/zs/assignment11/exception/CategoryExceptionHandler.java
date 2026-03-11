package com.zs.assignment11.exception;

import com.zs.assignment11.controller.CategoryController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for Category-related errors.
 */
@RestControllerAdvice(assignableTypes = CategoryController.class)
public class CategoryExceptionHandler {

}
