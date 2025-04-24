package com.william.puzzle.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
public class Game {
    private UUID gameId;
    private UUID userId;
    private String name;
    private Board board;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
}
