package com.zs.assignment11.exception;

/**
 * The type Cannot create product table exception.
 */
public class CannotCreateProductTableException extends RuntimeException {

    /**
     * Instantiates a new Cannot create product table exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public CannotCreateProductTableException(String message, Throwable cause) {
        super(message, cause);
    }
}
