package com.zs.assignment5.exceptions;

/**
 * The type Git log exception.
 */
public class GitLogException extends Exception {
    /**
     * Instantiates a new Git log exception.
     *
     * @param message the message
     */
    public GitLogException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Git log exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public GitLogException(String message, Throwable cause) {
        super(message, cause);
    }
}