package com.zs.assignment5.exceptions;

/**
 * Thrown when a commit block is missing vital information (e.g., missing author or date).
 */
public class FileInformationIncompeleteException extends GitLogException {
    public FileInformationIncompeleteException(String message) {
        super(message);
    }
}