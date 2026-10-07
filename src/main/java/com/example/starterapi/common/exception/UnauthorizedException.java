package com.example.starterapi.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when authentication fails or token is invalid.
 * Maps to HTTP 401 UNAUTHORIZED.
 */
public class UnauthorizedException extends ApiException {

    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
