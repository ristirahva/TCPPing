package ru.home.tcpping.exception;

/**
 * Exception thrown when something is wrong with command line arguments.
 */
public class InvalidArgumentException extends TcpPingException {
    public InvalidArgumentException(String message) {
        super(message);
    }
}
