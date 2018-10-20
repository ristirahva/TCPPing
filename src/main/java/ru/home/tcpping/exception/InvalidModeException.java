package ru.home.tcpping.exception;

/**
 * Exception throwed when TCPPing mode is incorrect.
 */
public class InvalidModeException extends TcpPingException {
    public InvalidModeException(String message) {
        super(message);
    }
}
