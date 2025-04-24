package com.william.puzzle.service.store;

import com.william.puzzle.enums.Role;
import com.william.puzzle.exception.user.ExistingUserException;
import com.william.puzzle.model.User;
import com.william.puzzle.service.PasswordServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class UserStoreServiceImpl implements UserStoreService {
    private final PasswordServiceImpl passwordService;
    private Map<UUID, User> userStore = new ConcurrentHashMap<>();
    private Map<String, UUID> nameToIdMap = new ConcurrentHashMap<>();

    public UUID addUser(String username, String password, Role role) {
        var id = UUID.randomUUID();
        var encryptedPassword = this.passwordService.encode(password);
        var newUser = new User(id, username, encryptedPassword, role);
        if (nameToIdMap.putIfAbsent(username, id) != null) {
            throw new ExistingUserException(username);
        }
        this.userStore.put(id, newUser);

        return id;
    }

    public User getUser(String username, String passwordAttempt) {
        var userId = getUserId(username);
        return Optional.ofNullable(userStore.get(userId))
                .filter(user -> this.passwordService.matches(passwordAttempt, user.getEncodedPassword()))
                .orElse(null);
    }

    public User getUser(String username) {
        var userId = getUserId(username);
        return userStore.get(userId);
    }

    public User getUser(UUID userId) {
        return userStore.get(userId);
    }

    public String getUserName(UUID userId) {
        return userStore.get(userId).getUsername();
    }

    public synchronized boolean deleteUser(UUID userId) {
        var deletedUser = userStore.remove(userId);
        if (deletedUser == null) {
            return false;
        }
        var mappedId = nameToIdMap.get(deletedUser.getUsername());
        if (userId.equals(mappedId)) {
            nameToIdMap.remove(deletedUser.getUsername());
        }
        return true;
    }

    public List<User> getAllUsers() {
        return userStore.values().stream()
                .filter(user -> user.getRole().equals(Role.PLAYER))
                .toList();

    }

    public List<UUID> deleteUsersNotLoggedInSinceDate(LocalDateTime cutoffTime) {
        List<UUID> userIdsToDelete = new ArrayList<>();
        userStore.forEach((userId, user) -> {
            if (user.getLastLogin().isBefore(cutoffTime)) {
                userIdsToDelete.add(userId);
            }
        });

        userIdsToDelete.forEach(userId -> {
            var deletedUser = userStore.remove(userId);
            if (deletedUser != null) {
                nameToIdMap.remove(deletedUser.getUsername());
            }
        });

        return userIdsToDelete;
    }

    private UUID getUserId(String username) {
        return nameToIdMap.get(username);
    }
}
