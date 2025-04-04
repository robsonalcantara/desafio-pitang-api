package com.pitang.desafiopitangapi.infra;

import com.pitang.desafiopitangapi.exceptions.InvalidTokenException;
import jakarta.persistence.EntityNotFoundException;
import com.pitang.desafiopitangapi.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for REST API responses. Handles specific exceptions
 * by providing structured error messages with appropriate HTTP status codes.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles exceptions for entities not found in the database.
     *
     * @author Robson Rodrigues
     * @param exception the exception thrown when an entity is not found
     * @return a ResponseEntity containing a REST error message with a 404 NOT FOUND status
     */
    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<RestErrorMessage> entityNotFoundHandler(EntityNotFoundException exception) {
        RestErrorMessage threatResponse = new RestErrorMessage(exception.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(threatResponse.getStatus()).body(threatResponse);
    }

    /**
     * Handles custom business exceptions.
     *
     * @author Robson Rodrigues
     * @param exception the BusinessException thrown during business rule violations
     * @return a ResponseEntity containing a REST error message with the specified HTTP status
     */
    @ExceptionHandler(BusinessException.class)
    private ResponseEntity<RestErrorMessage> businessExceptionHandler(BusinessException exception) {
        RestErrorMessage threatResponse = new RestErrorMessage(exception.getMessage(), exception.getStatus());
        return ResponseEntity.status(exception.getStatus()).body(threatResponse);
    }

    /**
     * Handles generic runtime exceptions with an unauthorized status.
     *
     * @param exception the RuntimeException thrown at runtime
     * @return a ResponseEntity containing a REST error message with a 401 UNAUTHORIZED status
     */
    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<RestErrorMessage> runtimeExceptionHandler(RuntimeException exception) {
        RestErrorMessage threatResponse = new RestErrorMessage(exception.getMessage(), HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(threatResponse.getStatus()).body(threatResponse);
    }

    /**
     * Handles invalid token exceptions during authentication.
     *
     * @param exception the InvalidTokenException indicating an invalid or expired token
     * @return a ResponseEntity containing a REST error message with a 401 UNAUTHORIZED status
     */
    @ExceptionHandler(InvalidTokenException.class)
    private ResponseEntity<RestErrorMessage> invalidTokenExceptionHandler(InvalidTokenException exception) {
        RestErrorMessage threatResponse = new RestErrorMessage(exception.getMessage(), HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(threatResponse.getStatus()).body(threatResponse);
    }

    /**
     * Handles bad credentials exceptions during authentication.
     *
     * @param exception the BadCredentialsException indicating invalid credentials
     * @return a ResponseEntity containing a REST error message with a 401 UNAUTHORIZED status
     */
    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<RestErrorMessage> badCredentialsExceptionHandler(BadCredentialsException exception) {
        RestErrorMessage threatResponse = new RestErrorMessage(exception.getMessage(), HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(threatResponse.getStatus()).body(threatResponse);
    }
}
