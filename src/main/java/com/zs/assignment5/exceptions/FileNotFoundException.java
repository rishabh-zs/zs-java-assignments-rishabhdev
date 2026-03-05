package com.zs.assignment5.exceptions;

/**
 * Thrown when the specified Git log file cannot be found.
 * Note: Named exactly as requested, residing in our custom package to avoid
 * collision with java.io.FileNotFoundException.
 */
public class FileNotFoundException extends GitLogException {
    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}