package com.zs.assignment5.exceptions;

/**
 * Checked exception used for domain-specific errors in the Git log analyzer.
 * <p>
 * Use this exception to represent recoverable failures such as invalid input,
 * log parsing problems, or processing errors that should be surfaced with a
 * clear user-facing message.
 */
public class GitLogException extends Exception {
    /**
     * Creates an exception with a descriptive message.
     *
     * @param message explanation of the Git log processing failure
     */
    public GitLogException(String message) {
        super(message);
    }

    /**
     * Creates an exception with a descriptive message and root cause.
     *
     * @param message explanation of the Git log processing failure
     * @param cause   underlying exception that triggered this failure
     */
    public GitLogException(String message, Throwable cause) {
        super(message, cause);
    }
}
