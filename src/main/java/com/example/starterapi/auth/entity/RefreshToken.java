package com.example.starterapi.auth.entity;

import com.example.starterapi.common.entity.BaseAuditableEntity;
import com.example.starterapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Entity storing cryptographically secure refresh tokens.
 * Used to issue new JWT access tokens without requiring re-authentication.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "refresh_tokens", indexes = {
        @Index(name = "idx_refresh_token", columnList = "token", unique = true)
})
public class RefreshToken extends BaseAuditableEntity {

    @Column(nullable = false, unique = true, length = 120)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    @Builder.Default
    private boolean revoked = false;

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiryDate);
    }
}
