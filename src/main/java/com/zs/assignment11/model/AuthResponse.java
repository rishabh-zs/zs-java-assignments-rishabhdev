package com.zs.assignment11.model;

/**
 * Login response payload containing the issued JWT.
 */
public record AuthResponse(String token) {
}

