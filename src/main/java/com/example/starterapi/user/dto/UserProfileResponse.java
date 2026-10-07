package com.example.starterapi.user.dto;

import com.example.starterapi.user.entity.Role;
import com.example.starterapi.user.entity.User;

import java.time.Instant;

/**
 * Immutable DTO record representing public user profile information.
 */
public record UserProfileResponse(
        Long id,
        String name,
        String username,
        String email,
        Role role,
        boolean verified,
        Instant createdAt
) {
    public static UserProfileResponse from(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isVerified(),
                user.getCreatedAt()
        );
    }
}
