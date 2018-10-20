package ru.home.tcpping.exception;

/**
 * Thrown when the message has invalid format.
 */
public class InvalidMessageFormatException extends TcpPingException{
    public InvalidMessageFormatException(String message) {
        super(message);
    }
}
