package com.william.puzzle.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        Long expiresAt,
        String username,
        String role
) {}
