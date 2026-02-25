package com.zs.assignment5.exceptions;

//Specific exception thrown when the git log format does not match the expected format, such as missing fields or incorrect delimiters.
public class FileFormatEntryException extends GitLogException {
    public FileFormatEntryException(String message) {
        super(message);
    }
}