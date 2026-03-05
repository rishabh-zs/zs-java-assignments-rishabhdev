package com.zs.assignment5.exceptions;

/**
 * Thrown when the Git log file does not follow the expected standard format.
 */
public class FileFormatException extends GitLogException {
    public FileFormatException(String message) {
        super(message);
    }
}