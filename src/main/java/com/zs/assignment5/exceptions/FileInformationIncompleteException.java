package com.zs.assignment5.exceptions;

/**
 * Thrown when a Git log entry is syntactically valid but missing required data.
 * <p>
 * Examples include missing author, commit hash, timestamp, or message fields
 * needed for analysis.
 */
public class FileInformationIncompleteException extends GitLogException {
    /**
     * Creates an exception with a descriptive message.
     *
     * @param message explanation of the missing or incomplete information
     */
    public FileInformationIncompleteException(String message) {
        super(message);
    }
}
