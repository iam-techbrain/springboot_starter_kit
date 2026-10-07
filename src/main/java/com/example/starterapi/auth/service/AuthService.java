package com.example.starterapi.auth.service;

import com.example.starterapi.auth.dto.*;

/**
 * Service contract for authentication and token lifecycle operations.
 * Demonstrates OOP Abstraction.
 */
public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    VerifyEmailResponse verifyEmail(String token);

    AuthResponse refreshToken(RefreshTokenRequest request);

    void resendVerification(ResendVerificationRequest request);
}
