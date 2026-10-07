package com.example.starterapi.user.entity;

import com.example.starterapi.common.entity.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * User Entity demonstrating OOP Inheritance from BaseAuditableEntity.
 * Inherits standard primary key, timestamps, versioning, and soft-delete flag.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email", unique = true),
        @Index(name = "idx_user_username", columnList = "username", unique = true)
})
public class User extends BaseAuditableEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Role role = Role.ROLE_USER;

    @Column(nullable = false)
    @Builder.Default
    private boolean verified = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;
}
