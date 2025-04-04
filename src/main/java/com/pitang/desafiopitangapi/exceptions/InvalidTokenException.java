package com.pitang.desafiopitangapi.exceptions;

/**
 * Custom exception to handle invalid token errors.
 * This exception is thrown when an invalid or expired token is encountered.
 */
public class InvalidTokenException extends RuntimeException {

    /**
     * Constructs a new InvalidTokenException with a detailed message.
     *
     * @author Robson Rodrigues
     * @param message the detail message explaining the reason for the exception
     */
    public InvalidTokenException(String message) {
        super(message);
    }
}
