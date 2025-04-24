package com.william.puzzle.service;

import com.william.puzzle.dto.GameResponse;
import com.william.puzzle.dto.MassDeletionResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for handling operations related to game management,
 * including CRUD operations, game moves, and administrative deletions.
 */
public interface GameService {

    /**
     * Retrieves all games associated with a specific user.
     *
     * @param userId the UUID of the user
     * @return a list of game response DTOs
     */
    List<GameResponse> getGamesByUser(UUID userId);

    /**
     * Creates a new game for a user.
     *
     * @param userId the UUID of the user
     * @param name   optional name of the game
     * @return the created game as a response DTO
     */
    GameResponse createGame(UUID userId, String name);

    /**
     * Retrieves a specific game by its ID for a given user.
     *
     * @param userId the UUID of the user
     * @param gameId the UUID of the game
     * @return the game response DTO
     */
    GameResponse getGame(UUID userId, UUID gameId);

    /**
     * Deletes a game by its ID for a user. Can optionally trigger an admin notification.
     *
     * @param userId         the UUID of the user
     * @param gameId         the UUID of the game
     * @param adminDeletion  if true, notifies admins about the deletion
     */
    void deleteGame(UUID userId, UUID gameId, boolean adminDeletion);

    /**
     * Makes a move in the specified game from the given square.
     *
     * @param userId     the UUID of the user
     * @param gameId     the UUID of the game
     * @param fromSquare the square index to move from
     * @return updated game response DTO
     */
    GameResponse makeMove(UUID userId, UUID gameId, Integer fromSquare);

    /**
     * Retrieves all games across all users.
     *
     * @return a list of all game response DTOs
     */
    List<GameResponse> getAllGames();

    /**
     * Deletes all completed games across all users.
     *
     * @return a response containing the count of deleted games
     */
    MassDeletionResponse deleteCompletedGames();

    /**
     * Deletes all games that haven't been played since a given date.
     *
     * @param dateTime the cutoff date for inactivity
     * @return a response containing the count of deleted games
     */
    MassDeletionResponse deleteGamesNotPlayedSinceDate(LocalDateTime dateTime);
}
