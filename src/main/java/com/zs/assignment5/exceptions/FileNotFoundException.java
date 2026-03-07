package com.zs.assignment5.exceptions;

/**
 * Thrown when the expected Git log file cannot be located or accessed.
 * <p>
 * This covers missing files, invalid paths, or permission issues that prevent
 * reading the input.
 */
public class FileNotFoundException extends GitLogException {
    /**
     * Creates an exception with a descriptive message.
     *
     * @param message explanation of why the file could not be found or opened
     */
    public FileNotFoundException(String message) {
        super(message);
    }

    /**
     * Creates an exception with a descriptive message and root cause.
     *
     * @param message explanation of why the file could not be found or opened
     * @param cause   underlying exception that triggered this failure
     */
    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
