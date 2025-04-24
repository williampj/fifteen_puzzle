package com.william.puzzle.service.store;

import com.william.puzzle.model.Board;
import com.william.puzzle.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GameStoreServiceTest {

    private GameStoreServiceImpl gameStoreService;

    @BeforeEach
    void setUp() {
        gameStoreService = new GameStoreServiceImpl();
    }

    @Test
    void addAndGetGame_ReturnsCorrectGame() {
        UUID userId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();

        Game game = Game.builder()
                .gameId(gameId)
                .userId(userId)
                .board(Board.builder().isSolved(false).build())
                .name("Test Game")
                .createdAt(LocalDateTime.now())
                .lastUpdatedAt(LocalDateTime.now())
                .build();

        gameStoreService.addGame(userId, game);

        Optional<Game> retrieved = gameStoreService.getGame(userId, gameId);

        assertTrue(retrieved.isPresent());
        assertEquals(gameId, retrieved.get().getGameId());
    }

    @Test
    void getGamesByUser_ReturnsCorrectList() {
        UUID userId = UUID.randomUUID();

        Game game1 = Game.builder().gameId(UUID.randomUUID()).userId(userId).board(Board.builder().build()).build();
        Game game2 = Game.builder().gameId(UUID.randomUUID()).userId(userId).board(Board.builder().build()).build();

        gameStoreService.addGame(userId, game1);
        gameStoreService.addGame(userId, game2);

        List<Game> games = gameStoreService.getGamesByUser(userId);

        assertEquals(2, games.size());
    }

    @Test
    void deleteGame_RemovesGameFromStore() {
        UUID userId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();

        Game game = Game.builder()
                .gameId(gameId)
                .userId(userId)
                .board(Board.builder().isSolved(false).build())
                .build();

        gameStoreService.addGame(userId, game);

        Optional<Game> deleted = gameStoreService.deleteGame(userId, gameId);

        assertTrue(deleted.isPresent());
        assertEquals(gameId, deleted.get().getGameId());
        assertTrue(gameStoreService.getGamesByUser(userId).isEmpty());
    }

    @Test
    void deleteGame_ReturnsEmpty_WhenGameNotFound() {
        UUID userId = UUID.randomUUID();
        UUID nonexistentGameId = UUID.randomUUID();

        Optional<Game> result = gameStoreService.deleteGame(userId, nonexistentGameId);

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteGamesByUser_RemovesAllGamesForUser() {
        UUID userId = UUID.randomUUID();
        Game game = Game.builder().gameId(UUID.randomUUID()).userId(userId).board(Board.builder().build()).build();

        gameStoreService.addGame(userId, game);
        gameStoreService.deleteGamesByUser(userId);

        assertTrue(gameStoreService.getGamesByUser(userId).isEmpty());
    }

    @Test
    void getAllGames_ReturnsAllStoredGames() {
        UUID user1 = UUID.randomUUID();
        UUID user2 = UUID.randomUUID();

        gameStoreService.addGame(user1, Game.builder().gameId(UUID.randomUUID()).userId(user1).board(Board.builder().build()).build());
        gameStoreService.addGame(user2, Game.builder().gameId(UUID.randomUUID()).userId(user2).board(Board.builder().build()).build());

        List<Game> allGames = gameStoreService.getAllGames();

        assertEquals(2, allGames.size());
    }

    @Test
    void deleteCompletedGames_RemovesOnlySolvedGames() {
        UUID userId = UUID.randomUUID();
        Game solvedGame = Game.builder().gameId(UUID.randomUUID()).userId(userId)
                .board(Board.builder().isSolved(true).build()).build();
        Game unsolvedGame = Game.builder().gameId(UUID.randomUUID()).userId(userId)
                .board(Board.builder().isSolved(false).build()).build();

        gameStoreService.addGame(userId, solvedGame);
        gameStoreService.addGame(userId, unsolvedGame);

        int deleted = gameStoreService.deleteCompletedGames();

        assertEquals(1, deleted);
        List<Game> remaining = gameStoreService.getGamesByUser(userId);
        assertEquals(1, remaining.size());
        assertFalse(remaining.get(0).getBoard().isSolved());
    }

    @Test
    void deleteGamesNotPlayedSinceDate_RemovesOnlyOldGames() {
        UUID userId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Game recentGame = Game.builder()
                .gameId(UUID.randomUUID())
                .userId(userId)
                .lastUpdatedAt(now)
                .board(Board.builder().build())
                .build();

        Game oldGame = Game.builder()
                .gameId(UUID.randomUUID())
                .userId(userId)
                .lastUpdatedAt(now.minusDays(10))
                .board(Board.builder().build())
                .build();

        gameStoreService.addGame(userId, recentGame);
        gameStoreService.addGame(userId, oldGame);

        int deleted = gameStoreService.deleteGamesNotPlayedSinceDate(now.minusDays(5));

        assertEquals(1, deleted);
        List<Game> remaining = gameStoreService.getGamesByUser(userId);
        assertEquals(1, remaining.size());
        assertEquals(recentGame.getGameId(), remaining.get(0).getGameId());
    }
}
