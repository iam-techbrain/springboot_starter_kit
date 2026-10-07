package com.example.starterapi.auth.service;

import com.example.starterapi.auth.dto.*;
import com.example.starterapi.auth.entity.RefreshToken;
import com.example.starterapi.auth.entity.TokenType;
import com.example.starterapi.auth.entity.VerificationToken;
import com.example.starterapi.auth.repository.RefreshTokenRepository;
import com.example.starterapi.auth.repository.VerificationTokenRepository;
import com.example.starterapi.common.exception.BadRequestException;
import com.example.starterapi.common.exception.ResourceNotFoundException;
import com.example.starterapi.common.exception.UnauthorizedException;
import com.example.starterapi.security.jwt.JwtTokenProvider;
import com.example.starterapi.user.dto.UserProfileResponse;
import com.example.starterapi.user.entity.Role;
import com.example.starterapi.user.entity.User;
import com.example.starterapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.UUID;

/**
 * Implementation of AuthService.
 * Handles user registration, credentials verification, JWT issuance,
 * email token verification, and refresh token rotation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${app.jwt.refresh-token-expiration-ms:604800000}")
    private long refreshTokenExpirationMs;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("An account with email " + request.email() + " already exists");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new BadRequestException("Username " + request.username() + " is already taken");
        }

        User user = User.builder()
                .name(request.name())
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_USER)
                .verified(false)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);

        // Generate email verification token (valid for 24 hours)
        createVerificationToken(savedUser);

        // Generate JWT credentials
        String accessToken = jwtTokenProvider.generateTokenForUsername(
                savedUser.getUsername(),
                Collections.singletonList(savedUser.getRole().getAuthority())
        );
        RefreshToken refreshToken = createRefreshToken(savedUser);

        log.info("User registered successfully with ID: {}", savedUser.getId());

        return AuthResponse.of(
                accessToken,
                refreshToken.getToken(),
                jwtTokenProvider.getExpirationTimeMs(),
                UserProfileResponse.from(savedUser)
        );
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.usernameOrEmail(), request.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);

        User user = userRepository.findByEmailOrUsername(request.usernameOrEmail(), request.usernameOrEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        RefreshToken refreshToken = createRefreshToken(user);

        log.info("User logged in successfully: {}", user.getUsername());

        return AuthResponse.of(
                accessToken,
                refreshToken.getToken(),
                jwtTokenProvider.getExpirationTimeMs(),
                UserProfileResponse.from(user)
        );
    }

    @Override
    @Transactional
    public VerifyEmailResponse verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository
                .findByTokenAndTokenType(token, TokenType.EMAIL_VERIFICATION)
                .orElseThrow(() -> new BadRequestException("Invalid verification token"));

        if (verificationToken.isUsed()) {
            throw new BadRequestException("Verification token has already been used");
        }

        if (verificationToken.isExpired()) {
            throw new BadRequestException("Verification token has expired. Please request a new one.");
        }

        User user = verificationToken.getUser();
        user.setVerified(true);
        userRepository.save(user);

        verificationToken.setUsed(true);
        verificationTokenRepository.save(verificationToken);

        log.info("User email verified successfully for: {}", user.getEmail());

        return new VerifyEmailResponse(true, "Email verified successfully", user.getEmail());
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (refreshToken.isRevoked()) {
            throw new UnauthorizedException("Refresh token has been revoked");
        }

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new UnauthorizedException("Refresh token has expired. Please login again.");
        }

        User user = refreshToken.getUser();

        String newAccessToken = jwtTokenProvider.generateTokenForUsername(
                user.getUsername(),
                Collections.singletonList(user.getRole().getAuthority())
        );

        return AuthResponse.of(
                newAccessToken,
                refreshToken.getToken(),
                jwtTokenProvider.getExpirationTimeMs(),
                UserProfileResponse.from(user)
        );
    }

    @Override
    @Transactional
    public void resendVerification(ResendVerificationRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.email()));

        if (user.isVerified()) {
            throw new BadRequestException("User account is already verified");
        }

        createVerificationToken(user);
        log.info("New verification token generated for email: {}", user.getEmail());
    }

    private VerificationToken createVerificationToken(User user) {
        String tokenString = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(24, ChronoUnit.HOURS);

        VerificationToken token = VerificationToken.builder()
                .token(tokenString)
                .user(user)
                .tokenType(TokenType.EMAIL_VERIFICATION)
                .expiryDate(expiryDate)
                .used(false)
                .build();

        return verificationTokenRepository.save(token);
    }

    private RefreshToken createRefreshToken(User user) {
        String tokenString = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plusMillis(refreshTokenExpirationMs);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(tokenString)
                .user(user)
                .expiryDate(expiryDate)
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }
}
