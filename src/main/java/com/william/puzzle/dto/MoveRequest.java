package com.william.puzzle.dto;

import jakarta.validation.constraints.NotNull;

public record MoveRequest(@NotNull Integer fromSquare) {}
