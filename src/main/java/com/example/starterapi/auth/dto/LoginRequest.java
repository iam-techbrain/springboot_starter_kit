package com.example.starterapi.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for user authentication.
 */
public record LoginRequest(
        @NotBlank(message = "Email or username is required")
        String usernameOrEmail,

        @NotBlank(message = "Password is required")
        String password
) {}
