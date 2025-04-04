package com.pitang.desafiopitangapi.infra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * Represents a structured error message returned in REST API responses.
 * Contains a message describing the error and an HTTP status code.
 */
@Getter @Setter @AllArgsConstructor
public class RestErrorMessage {

    /**
     * A descriptive message about the error.
     */
    private String message;

    /**
     * The HTTP status associated with the error.
     */
    private HttpStatus status;
}
