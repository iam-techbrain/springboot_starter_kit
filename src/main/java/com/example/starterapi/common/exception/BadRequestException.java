package com.example.starterapi.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when incoming request logic or payload is invalid.
 * Maps to HTTP 400 BAD_REQUEST.
 */
public class BadRequestException extends ApiException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, cause);
    }
}
