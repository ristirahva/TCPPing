package ru.home.tcpping.exception;

/**
 * Base application exception.
 */
public class TcpPingException extends RuntimeException {
    public TcpPingException(String message) {
        super(message);
    }
}
