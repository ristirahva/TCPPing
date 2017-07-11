package com.infobip.tcpping.exception;

/**
 * Exception throwed when something is wrong with command line arguments.
 */
public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException(String message) {
        super(message);
    }
}
