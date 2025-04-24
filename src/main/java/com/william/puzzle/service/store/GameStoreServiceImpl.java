package com.william.puzzle.service.store;

import com.william.puzzle.model.Game;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Optional;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

@Service
public class GameStoreServiceImpl {
    private final Map<UUID, List<Game>> storedGames = new ConcurrentHashMap<>();

    public void addGame(UUID userId, Game game) {
        storedGames.computeIfAbsent(userId, k -> Collections.synchronizedList(new ArrayList<>())).add(game);
    }

    public Optional<Game> getGame(UUID userId, UUID gameId) {
        return Optional.ofNullable(storedGames.get(userId))
            .flatMap(games -> games.stream()
                .filter(game -> game.getGameId().equals(gameId))
                .findFirst());
    }

    public List<Game> getGamesByUser(UUID userId) {
        return storedGames.getOrDefault(userId, List.of());
    }

    public Optional<Game> deleteGame(UUID userId, UUID gameId) {
        var gamesList = getGamesByUser(userId);

        for (var iterator = gamesList.iterator(); iterator.hasNext(); ) {
            var game = iterator.next();
            if (game.getGameId().equals(gameId)) {
                iterator.remove();
                return Optional.of(game);
            }
        }

        return Optional.empty();
    }

    public void deleteGamesByUser(UUID userId) {
        storedGames.remove(userId);
    }

    public List<Game> getAllGames() {
        return storedGames.values().stream()
                .flatMap(List::stream)
                .toList();
    }

    public int deleteCompletedGames() {
        Predicate<Game> isCompleted = game -> game.getBoard().isSolved();
        return deleteGamesMatchingCriteria(isCompleted);
    }

    public int deleteGamesNotPlayedSinceDate(LocalDateTime dateTime) {
        Predicate<Game> notOpenedSinceGivenDate = game -> game.getLastUpdatedAt().isBefore(dateTime);
        return deleteGamesMatchingCriteria(notOpenedSinceGivenDate);
    }

    private int deleteGamesMatchingCriteria(Predicate<Game> criteria) {
        var deletionCount = new AtomicInteger(0);
        var entriesToDelete = new ArrayList<>();

        storedGames.forEach((userId, gamesList) -> {
            var iterator = gamesList.iterator();
            while (iterator.hasNext()) {
                var game = iterator.next();
                if (criteria.test(game)) {
                    iterator.remove();
                    deletionCount.incrementAndGet();
                }
            }
            if (gamesList.isEmpty()) {
                entriesToDelete.add(userId);
            }
        });
        entriesToDelete.forEach(storedGames::remove);

        return deletionCount.get();
    }
}
