package com.william.puzzle.controller;

import com.william.puzzle.constants.ApiPaths;
import com.william.puzzle.dto.CreateGameRequest;
import com.william.puzzle.dto.MoveRequest;
import com.william.puzzle.dto.GameResponse;
import com.william.puzzle.model.Game;
import com.william.puzzle.model.User;
import com.william.puzzle.service.GameServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.GAMES)
public class GameController {
    private final GameServiceImpl gameService;

    private static HashMap<UUID, List<Game>> gameStore;

    @GetMapping
    ResponseEntity<List<GameResponse>> getUsersGames() {
        var games = gameService.getGamesByUser(getUserId());
        return ResponseEntity.status(200).body(games);
    }

    @PostMapping
    ResponseEntity<GameResponse> createGame(@Valid @RequestBody CreateGameRequest request) {
        var gameResponse = gameService.createGame(getUserId(), request.name());
        return ResponseEntity.status(201).body(gameResponse);
    }

    @GetMapping(ApiPaths.GET_GAME)
    ResponseEntity<GameResponse> getGame(@PathVariable UUID gameId) {
        var gameResponse = gameService.getGame(getUserId(), gameId);
        return ResponseEntity.status(200).body(gameResponse);
    }

    @PostMapping(ApiPaths.MOVE)
    ResponseEntity<GameResponse> makeMove(@PathVariable UUID gameId, @Valid @RequestBody MoveRequest request) {
        var userId = getUserId();
        var gameResponse = gameService.makeMove(userId, gameId, request.fromSquare());
        return ResponseEntity.status(200).body(gameResponse);
    }

    @DeleteMapping(ApiPaths.DELETE_GAME)
    ResponseEntity<Void> deleteGame(@PathVariable UUID gameId) {
        gameService.deleteGame(getUserId(), gameId, false);
        return ResponseEntity.status(204).build();
    }

    private UUID getUserId() {
        return getUser().getId();
    }

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
