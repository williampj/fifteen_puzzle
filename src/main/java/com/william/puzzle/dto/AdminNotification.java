package com.william.puzzle.dto;

import com.william.puzzle.enums.AdminNotificationType;

import java.util.UUID;

public record AdminNotification (
        UUID userId,
        AdminNotificationType type,
        String message
) {}
