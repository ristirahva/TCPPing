package com.infobip.tcpping.exception;

/**
 * Wrapper for Pitcher checked exceptions.
 */
public class PitcherExecutionException extends RuntimeException {
    public PitcherExecutionException(String message) {
        super(message);
    }
}
