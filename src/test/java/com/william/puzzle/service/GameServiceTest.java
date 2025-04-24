package com.william.puzzle.service;

import com.william.puzzle.enums.AdminNotificationType;
import com.william.puzzle.exception.game.GameNotFoundException;
import com.william.puzzle.exception.game.InvalidBoardMoveException;
import com.william.puzzle.model.Board;
import com.william.puzzle.model.Game;
import com.william.puzzle.service.store.GameStoreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {

    @Mock private GameStoreServiceImpl gameStoreService;
    @Mock private BoardServiceImpl boardService;
    @Mock private AdminNotificationPublisherImpl adminNotificationPublisher;

    @InjectMocks private GameServiceImpl gameService;

    private UUID userId;
    private UUID gameId;
    private Game testGame;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        gameId = UUID.randomUUID();
        var board = Board.builder().gameId(gameId).lineup(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0)).isSolved(true).build();
        testGame = Game.builder()
                .gameId(gameId)
                .userId(userId)
                .name("TestGame")
                .board(board)
                .createdAt(LocalDateTime.now())
                .lastUpdatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldReturnGamesByUser() {
        when(gameStoreService.getGamesByUser(userId)).thenReturn(List.of(testGame));
        var result = gameService.getGamesByUser(userId);
        assertEquals(1, result.size());
        assertEquals(testGame.getName(), result.getFirst().name());
    }

    @Test
    void shouldGetGameById() {
        when(gameStoreService.getGame(userId, gameId)).thenReturn(Optional.of(testGame));
        var result = gameService.getGame(userId, gameId);
        assertEquals(testGame.getGameId(), result.gameId());
    }

    @Test
    void shouldThrowIfGameNotFound() {
        when(gameStoreService.getGame(userId, gameId)).thenReturn(Optional.empty());
        assertThrows(GameNotFoundException.class, () -> gameService.getGame(userId, gameId));
    }

    @Test
    void shouldDeleteGameAndNotifyIfAdmin() {
        when(gameStoreService.deleteGame(userId, gameId)).thenReturn(Optional.of(testGame));
        gameService.deleteGame(userId, gameId, true);

        ArgumentCaptor<com.william.puzzle.dto.AdminNotification> captor = ArgumentCaptor.forClass(com.william.puzzle.dto.AdminNotification.class);
        verify(adminNotificationPublisher).sendGameDeletedNotification(captor.capture());
        assertEquals(AdminNotificationType.GAME_DELETED, captor.getValue().type());
    }

    @Test
    void shouldThrowWhenDeletingNonExistentGame() {
        when(gameStoreService.deleteGame(userId, gameId)).thenReturn(Optional.empty());
        assertThrows(GameNotFoundException.class, () -> gameService.deleteGame(userId, gameId, false));
    }

    @Test
    void shouldMakeValidMove() {
        when(gameStoreService.getGame(userId, gameId)).thenReturn(Optional.of(testGame));
        when(boardService.isValidMove(any(), eq(15))).thenReturn(true);

        var result = gameService.makeMove(userId, gameId, 15);
        assertEquals(testGame.getGameId(), result.gameId());
        verify(boardService).makeMove(testGame.getBoard(), 15);
    }

    @Test
    void shouldThrowOnInvalidMove() {
        when(gameStoreService.getGame(userId, gameId)).thenReturn(Optional.of(testGame));
        when(boardService.isValidMove(any(), eq(0))).thenReturn(false);
        assertThrows(InvalidBoardMoveException.class, () -> gameService.makeMove(userId, gameId, 0));
    }

    @Test
    void shouldDeleteCompletedGames() {
        when(gameStoreService.deleteCompletedGames()).thenReturn(3);
        var response = gameService.deleteCompletedGames();
        assertEquals(3, response.deletionCount());
    }

    @Test
    void shouldDeleteGamesNotPlayedSinceDate() {
        var cutoff = LocalDateTime.now().minusDays(30);
        when(gameStoreService.deleteGamesNotPlayedSinceDate(cutoff)).thenReturn(2);
        var response = gameService.deleteGamesNotPlayedSinceDate(cutoff);
        assertEquals(2, response.deletionCount());
    }
}
