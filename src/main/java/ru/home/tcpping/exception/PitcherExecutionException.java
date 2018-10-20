package ru.home.tcpping.exception;

/**
 * Wrapper for Pitcher checked exceptions.
 */
public class PitcherExecutionException extends TcpPingException {
    public PitcherExecutionException(String message) {
        super(message);
    }
}
