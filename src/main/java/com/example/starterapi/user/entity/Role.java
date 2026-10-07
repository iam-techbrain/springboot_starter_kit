package com.example.starterapi.user.entity;

/**
 * System roles for Role-Based Access Control (RBAC).
 */
public enum Role {
    ROLE_USER,
    ROLE_ADMIN;

    public String getAuthority() {
        return name();
    }
}
