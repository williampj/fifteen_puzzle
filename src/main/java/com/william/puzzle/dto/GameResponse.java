package com.william.puzzle.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GameResponse(
    UUID gameId,
    String name,
    boolean isSolved,
    LocalDateTime createdAt,
    LocalDateTime lastUpdatedAt,
    List<Integer> lineup
) {}
