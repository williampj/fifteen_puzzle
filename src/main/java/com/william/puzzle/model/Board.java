package com.william.puzzle.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
public class Board {
    UUID gameId;
    private List<Integer> lineup;
    private boolean isSolved;
}
