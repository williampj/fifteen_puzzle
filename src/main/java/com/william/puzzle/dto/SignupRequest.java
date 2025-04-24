package com.william.puzzle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest (
    @NotBlank() String username,
    @Size(min = 10, message = "Password length must be at least 10 characters") String password
) {}
