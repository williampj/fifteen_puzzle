package com.william.puzzle.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserAdminResponse(UUID id, String username, LocalDateTime lastLogin) {};
