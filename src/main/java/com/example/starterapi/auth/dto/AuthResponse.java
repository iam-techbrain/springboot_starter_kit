package com.example.starterapi.auth.dto;

import com.example.starterapi.user.dto.UserProfileResponse;

/**
 * Standard authentication response returned on successful login or registration.
 */
public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresInMs,
        UserProfileResponse user
) {
    public static AuthResponse of(String accessToken, String refreshToken, long expiresInMs, UserProfileResponse user) {
        return new AuthResponse(accessToken, refreshToken, "Bearer", expiresInMs, user);
    }
}
