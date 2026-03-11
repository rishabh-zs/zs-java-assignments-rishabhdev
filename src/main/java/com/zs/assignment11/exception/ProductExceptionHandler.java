package com.zs.assignment11.exception;

import com.zs.assignment11.controller.ProductController;
import com.zs.assignment11.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for Product-related errors.
 */
@RestControllerAdvice(assignableTypes = ProductController.class)
public class ProductExceptionHandler {
    private static final Logger log = LoggerUtil.getLogger(ProductExceptionHandler.class);

}
