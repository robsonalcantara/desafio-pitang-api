package com.pitang.desafiopitangapi.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Custom exception to handle business logic errors.
 * This exception is thrown when there is a specific error in business rules or constraints.
 * It includes an HTTP status to be used in the response.
 */
public class BusinessException extends RuntimeException {
    private final HttpStatus status;

    /**
     * Constructs a new BusinessException with a detailed message and an HTTP status.
     *
     * @author Robson Rodrigues
     * @param message the detail message explaining the exception
     * @param status the HTTP status associated with this exception
     */
    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    /**
     * Gets the HTTP status associated with this exception.
     *
     * @author Robson Rodrigues
     * @return the HTTP status
     */
    public HttpStatus getStatus() {
        return status;
    }
}
