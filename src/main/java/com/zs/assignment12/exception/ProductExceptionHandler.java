package com.zs.assignment12.exception;

import com.zs.assignment12.controller.ProductController;
import com.zs.assignment12.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for Product-related errors.
 */
@RestControllerAdvice(assignableTypes = ProductController.class)
public class ProductExceptionHandler {
    private static final Logger log = LoggerUtil.getLogger(ProductExceptionHandler.class);

}
