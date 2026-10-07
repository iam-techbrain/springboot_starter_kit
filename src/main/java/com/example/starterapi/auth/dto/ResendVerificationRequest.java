package com.example.starterapi.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResendVerificationRequest(
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Must be a valid email address")
        String email
) {}
