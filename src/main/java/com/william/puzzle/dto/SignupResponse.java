package com.william.puzzle.dto;

import java.util.UUID;

public record SignupResponse(
        UUID userId,
        String username,
        String message
) {}
