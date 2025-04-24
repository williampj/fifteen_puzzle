package com.william.puzzle.service.store;

import com.william.puzzle.model.Game;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface for managing the in-memory game store.
 */
public interface GameStoreService {

    /**
     * Adds a game to the store for a given user.
     *
     * @param userId the user ID
     * @param game   the game to add
     */
    void addGame(UUID userId, Game game);

    /**
     * Retrieves a specific game by user ID and game ID.
     *
     * @param userId the user ID
     * @param gameId the game ID
     * @return an Optional containing the game if found, or empty if not
     */
    Optional<Game> getGame(UUID userId, UUID gameId);

    /**
     * Retrieves all games for a specific user.
     *
     * @param userId the user ID
     * @return a list of games for the user
     */
    List<Game> getGamesByUser(UUID userId);

    /**
     * Deletes a specific game by user ID and game ID.
     *
     * @param userId the user ID
     * @param gameId the game ID
     * @return an Optional containing the deleted game if it was found, or empty if not
     */
    Optional<Game> deleteGame(UUID userId, UUID gameId);

    /**
     * Deletes all games associated with a user.
     *
     * @param userId the user ID
     */
    void deleteGamesByUser(UUID userId);

    /**
     * Retrieves all games from all users.
     *
     * @return a list of all games
     */
    List<Game> getAllGames();

    /**
     * Deletes all games that are completed (solved).
     *
     * @return the number of deleted games
     */
    int deleteCompletedGames();

    /**
     * Deletes all games that haven't been played since a given date.
     *
     * @param dateTime the cutoff datetime
     * @return the number of deleted games
     */
    int deleteGamesNotPlayedSinceDate(LocalDateTime dateTime);
}
