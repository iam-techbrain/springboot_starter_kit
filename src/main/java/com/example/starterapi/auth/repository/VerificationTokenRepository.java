package com.example.starterapi.auth.repository;

import com.example.starterapi.auth.entity.TokenType;
import com.example.starterapi.auth.entity.VerificationToken;
import com.example.starterapi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByTokenAndTokenType(String token, TokenType tokenType);

    Optional<VerificationToken> findByUserAndTokenTypeAndUsedFalse(User user, TokenType tokenType);
}
