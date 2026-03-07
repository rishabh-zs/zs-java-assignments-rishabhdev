package com.zs.assignment5.exceptions;

/**
 * Thrown when a Git log file is present but does not match the expected format.
 * <p>
 * Use this exception for structural issues such as missing delimiters,
 * malformed lines, or unsupported log layouts.
 */
public class FileFormatException extends GitLogException {
    /**
     * Creates an exception with a descriptive message.
     *
     * @param message explanation of the format violation
     */
    public FileFormatException(String message) {
        super(message);
    }
}
