package com.william.puzzle.dto;

import com.william.puzzle.enums.Role;
import java.util.UUID;

public record UserResponse (UUID id, String username, Role role) {};
