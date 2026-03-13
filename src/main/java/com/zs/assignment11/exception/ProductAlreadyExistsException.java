package com.zs.assignment11.exception;

public class ProductAlreadyExistsException extends RuntimeException {

    public ProductAlreadyExistsException(String productName) {
        super("Product already exists: " + productName);
    }
}

