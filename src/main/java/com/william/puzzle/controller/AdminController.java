package com.william.puzzle.controller;

import com.william.puzzle.constants.ApiPaths;
import com.william.puzzle.dto.GameResponse;
import com.william.puzzle.dto.MassDeletionResponse;
import com.william.puzzle.dto.UserAdminResponse;
import com.william.puzzle.service.GameServiceImpl;
import com.william.puzzle.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.ADMIN)
public class AdminController {
    private final GameServiceImpl gameService;
    private final UserServiceImpl userService;

    @GetMapping(ApiPaths.ALL_USERS)
    public ResponseEntity<List<UserAdminResponse>> getAllUsers() {
        var users = userService.getAllUsers();
        return ResponseEntity.status(200).body(users);
    }

    @GetMapping(ApiPaths.INDIVIDUAL_USER)
    public ResponseEntity<UserAdminResponse> getUser(@PathVariable UUID userId) {
        var users = userService.getUser(userId);
        return ResponseEntity.status(200).body(users);
    }

    @GetMapping(ApiPaths.ALL_GAMES)
    public ResponseEntity<List<GameResponse>> getAllGames() {
        var games = gameService.getAllGames();
        return ResponseEntity.status(200).body(games);
    }

    @GetMapping(ApiPaths.GAMES_BY_USER)
    public ResponseEntity<List<GameResponse>> getGamesByUser(@PathVariable UUID userId) {
        var games = gameService.getGamesByUser(userId);
        return ResponseEntity.status(200).body(games);
    }

    @GetMapping(ApiPaths.INDIVIDUAL_GAME)
    public ResponseEntity<GameResponse> getGame(@PathVariable UUID userId, @PathVariable UUID gameId) {
        var game = gameService.getGame(userId, gameId);
        return ResponseEntity.status(200).body(game);
    }

    @DeleteMapping(ApiPaths.INDIVIDUAL_USER)
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId, true);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping(ApiPaths.INDIVIDUAL_GAME)
    public ResponseEntity<Void> deleteGame(@PathVariable UUID userId, @PathVariable UUID gameId) {
        gameService.deleteGame(userId, gameId, true);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping(ApiPaths.COMPLETED_GAMES)
    public ResponseEntity<MassDeletionResponse> deleteCompletedGames() {
        var deletionResponse = gameService.deleteCompletedGames();
        return ResponseEntity.status(200).body(deletionResponse);
    }

    @DeleteMapping(ApiPaths.INACTIVE_GAMES)
    public ResponseEntity<MassDeletionResponse> deleteGamesNotPlayedSinceDate (
            @RequestParam("since") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate since) {
        var sinceDateTime = since.atStartOfDay();
        var deletionResponse = gameService.deleteGamesNotPlayedSinceDate(sinceDateTime);
        return ResponseEntity.status(200).body(deletionResponse);
    }

    @DeleteMapping(ApiPaths.INACTIVE_USERS)
    public ResponseEntity<MassDeletionResponse> deleteUsersNotLoggedInSinceDate (
            @RequestParam("since") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate since) {
        var sinceDateTime = since.atStartOfDay();
        var deletionResponse = userService.deleteUsersNotLoggedInSinceDate(sinceDateTime);
        return ResponseEntity.status(200).body(deletionResponse);
    }
}
