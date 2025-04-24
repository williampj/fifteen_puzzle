package com.william.puzzle.dto;

import jakarta.validation.constraints.Size;

public record CreateGameRequest(
   @Size(max = 40, message = "Name may not exceed 40 characters") String name
) {}
