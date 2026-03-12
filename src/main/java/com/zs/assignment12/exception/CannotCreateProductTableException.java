package com.zs.assignment12.exception;

public class CannotCreateProductTableException extends RuntimeException {

    public CannotCreateProductTableException(String message, Throwable cause) {
        super(message, cause);
    }
}
