package com.zs.assignment11.exception;

public class CannotGetAllProductByCategoryIdException extends RuntimeException {

    public CannotGetAllProductByCategoryIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
