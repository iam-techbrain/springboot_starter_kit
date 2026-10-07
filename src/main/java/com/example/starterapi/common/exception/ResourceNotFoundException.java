package com.example.starterapi.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when an entity or resource is not found in the database.
 * Maps to HTTP 404 NOT_FOUND.
 */
public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue), HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
