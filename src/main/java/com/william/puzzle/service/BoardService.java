package com.william.puzzle.service;

import com.william.puzzle.model.Board;

import java.util.UUID;

/**
 * Service interface for managing puzzle board operations.
 */
public interface BoardService {

    /**
     * Instantiates a new solvable puzzle board for a given game ID.
     *
     * @param gameId the unique identifier for the game
     * @return a new {@link Board} instance with a solvable lineup
     */
    Board instantiateBoard(UUID gameId);

    /**
     * Checks whether a move from the given square index is valid.
     *
     * @param board      the current state of the puzzle board
     * @param fromSquare the index of the square to move
     * @return true if the move is valid, false otherwise
     */
    boolean isValidMove(Board board, Integer fromSquare);

    /**
     * Performs a move on the board from the specified square.
     * This updates the board's lineup and solved status.
     *
     * @param board      the current state of the puzzle board
     * @param fromSquare the index of the square to move
     */
    void makeMove(Board board, Integer fromSquare);
}
