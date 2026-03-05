package com.zs.assignment5.exceptions;

/**
 * Base user-defined checked exception for Git Log parsing errors.
 */
public class GitLogException extends Exception {
    public GitLogException(String message) {
        super(message);
    }

    public GitLogException(String message, Throwable cause) {
        super(message, cause);
    }
}