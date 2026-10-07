package com.example.starterapi.auth.entity;

import com.example.starterapi.common.entity.BaseAuditableEntity;
import com.example.starterapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Entity for one-time verification tokens (email verification, account activation).
 * Extends BaseAuditableEntity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "verification_tokens", indexes = {
        @Index(name = "idx_verification_token", columnList = "token", unique = true)
})
public class VerificationToken extends BaseAuditableEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private TokenType tokenType = TokenType.EMAIL_VERIFICATION;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    @Builder.Default
    private boolean used = false;

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiryDate);
    }
}
