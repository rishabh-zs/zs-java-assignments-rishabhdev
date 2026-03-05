package com.zs.assignment5.exceptions;

/**
 * The type File not found exception.
 */
public class FileNotFoundException extends GitLogException {
    /**
     * Instantiates a new File not found exception.
     *
     * @param message the message
     */
    public FileNotFoundException(String message) {
        super(message);
    }

    /**
     * Instantiates a new File not found exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}