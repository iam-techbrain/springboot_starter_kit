package com.example.starterapi.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when an authenticated user does not have permission for the requested resource.
 * Maps to HTTP 403 FORBIDDEN.
 */
public class ForbiddenException extends ApiException {

    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
