package com.zs.assignment11.exception;

/**
 * The type Cannot create category table exception.
 */
public class CannotCreateCategoryTableException extends RuntimeException {

    /**
     * Instantiates a new Cannot create category table exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public CannotCreateCategoryTableException(String message, Throwable cause) {
        super(message, cause);
    }
}
