package com.william.puzzle.service;

import com.github.javafaker.Faker;
import com.william.puzzle.constants.ExceptionMessages;
import com.william.puzzle.dto.AdminNotification;
import com.william.puzzle.dto.GameResponse;
import com.william.puzzle.dto.MassDeletionResponse;
import com.william.puzzle.enums.AdminNotificationType;
import com.william.puzzle.exception.game.GameNotFoundException;
import com.william.puzzle.exception.game.InvalidBoardMoveException;
import com.william.puzzle.model.Game;
import com.william.puzzle.service.store.GameStoreServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameStoreServiceImpl gameStoreService;
    private final BoardServiceImpl boardService;
    private final AdminNotificationPublisherImpl adminNotificationPublisher;

    public List<GameResponse> getGamesByUser(UUID userId) {
        var games = gameStoreService.getGamesByUser(userId);
        return games.stream().map(this::mapToGameResponse).toList();
    }

    public GameResponse createGame(UUID userId, String name) {
        var newGame = instantiateGame(userId, name);
        gameStoreService.addGame(userId, newGame);

        return mapToGameResponse(newGame);
    }

    public GameResponse getGame(UUID userId, UUID gameId) {
        var game = getGameOrThrow(userId, gameId);
        return mapToGameResponse(game);
    }

    public void deleteGame(UUID userId, UUID gameId, boolean adminDeletion) {
        var deletedGame = gameStoreService.deleteGame(userId, gameId);
        if (deletedGame.isEmpty()) {
            throw new GameNotFoundException(ExceptionMessages.GAME_NOT_FOUND + ": " + gameId);
        }
        if (adminDeletion) {
            var name = deletedGame.get().getName();
            notifyOwnerOfGameDeletion(userId, name);
        }
    }

    public GameResponse makeMove(UUID userId, UUID gameId, Integer fromSquare) {
        var game = getGameOrThrow(userId, gameId);
        var board = game.getBoard();
        if (!boardService.isValidMove(board, fromSquare)) {
            throw new InvalidBoardMoveException(ExceptionMessages.INVALID_BOARD_MOVE);
        }
        boardService.makeMove(board, fromSquare);
        game.setLastUpdatedAt(LocalDateTime.now());

        return mapToGameResponse(game);
    }

    public List<GameResponse> getAllGames() {
        var games = gameStoreService.getAllGames();
        return games.stream().map(this::mapToGameResponse).toList();
    }

    public MassDeletionResponse deleteCompletedGames() {
        var deletionCount = gameStoreService.deleteCompletedGames();
        return new MassDeletionResponse(deletionCount);
    }

    public MassDeletionResponse deleteGamesNotPlayedSinceDate(LocalDateTime dateTime) {
        var deletionCount = gameStoreService.deleteGamesNotPlayedSinceDate(dateTime);
        return new MassDeletionResponse(deletionCount);
    }

    private Game instantiateGame(UUID userId, String name) {
        var gameId = UUID.randomUUID();
        var currentTime = LocalDateTime.now();
        var finalName = (name != null && !name.isBlank() ? name : generateName());

        return Game.builder()
                .gameId(gameId)
                .userId(userId)
                .name(finalName)
                .board(boardService.instantiateBoard(gameId))
                .createdAt(currentTime)
                .lastUpdatedAt(currentTime)
                .build();
    }

    private Game getGameOrThrow(UUID userId, UUID gameId) {
        var game = gameStoreService.getGame(userId, gameId);
        if (game.isEmpty()) {
            throw new GameNotFoundException(ExceptionMessages.GAME_NOT_FOUND + ": " + gameId);
        }

        return game.get();
    }

    private void notifyOwnerOfGameDeletion(UUID userId, String gameName) {
        var type = AdminNotificationType.GAME_DELETED;
        var message = type.format(gameName);
        var notification = new AdminNotification(userId, type, message);
        adminNotificationPublisher.sendGameDeletedNotification(notification);
    }

    private GameResponse mapToGameResponse(Game game) {
        var board = game.getBoard();
        return new GameResponse(
            game.getGameId(),
            game.getName(),
            board.isSolved(),
            game.getCreatedAt(),
            game.getLastUpdatedAt(),
            board.getLineup()
        );
    }

    private String generateName() {
        var faker = new Faker();
        return faker.color().name() + '-' + faker.app().name() + '-' + generateRandomInteger();
    }

    private int generateRandomInteger() {
        return ThreadLocalRandom.current().nextInt(0, 101);
    }
}

