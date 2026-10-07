package com.example.starterapi.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base abstract runtime exception for domain-level business errors.
 * Demonstrates OOP Exception hierarchy.
 */
@Getter
public abstract class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ApiException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
