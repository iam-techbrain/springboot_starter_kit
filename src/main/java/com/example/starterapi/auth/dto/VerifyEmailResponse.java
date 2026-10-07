package com.example.starterapi.auth.dto;

public record VerifyEmailResponse(
        boolean verified,
        String message,
        String email
) {}
