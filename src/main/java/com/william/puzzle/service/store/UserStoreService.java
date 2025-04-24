package com.william.puzzle.service.store;

import com.william.puzzle.enums.Role;
import com.william.puzzle.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Interface for storing and managing user data in memory.
 */
public interface UserStoreService {

    /**
     * Adds a new user to the store.
     *
     * @param username the username
     * @param password the plain text password
     * @param role     the user's role
     * @return the generated UUID for the new user
     * @throws com.william.puzzle.exception.user.ExistingUserException if the username already exists
     */
    UUID addUser(String username, String password, Role role);

    /**
     * Retrieves a user by username and validates the password attempt.
     *
     * @param username       the username
     * @param passwordAttempt the password attempt
     * @return the authenticated user, or {@code null} if authentication fails
     */
    User getUser(String username, String passwordAttempt);

    /**
     * Retrieves a user by username.
     *
     * @param username the username
     * @return the user, or {@code null} if not found
     */
    User getUser(String username);

    /**
     * Retrieves a user by their UUID.
     *
     * @param userId the user's UUID
     * @return the user, or {@code null} if not found
     */
    User getUser(UUID userId);

    /**
     * Returns the username for a given user ID.
     *
     * @param userId the user's UUID
     * @return the username, or {@code null} if not found
     */
    String getUserName(UUID userId);

    /**
     * Deletes a user by their UUID.
     *
     * @param userId the user's UUID
     * @return {@code true} if the user was successfully deleted, {@code false} otherwise
     */
    boolean deleteUser(UUID userId);

    /**
     * Retrieves all users with the {@code PLAYER} role.
     *
     * @return list of player users
     */
    List<User> getAllUsers();

    /**
     * Deletes users who haven't logged in since the given cutoff time.
     *
     * @param cutoffTime the cutoff {@link LocalDateTime}
     * @return a list of UUIDs of users that were deleted
     */
    List<UUID> deleteUsersNotLoggedInSinceDate(LocalDateTime cutoffTime);
}
