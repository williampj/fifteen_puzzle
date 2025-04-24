package com.william.puzzle.service;

import com.william.puzzle.dto.MassDeletionResponse;
import com.william.puzzle.dto.SignupRequest;
import com.william.puzzle.dto.SignupResponse;
import com.william.puzzle.dto.UserAdminResponse;
import com.william.puzzle.enums.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Interface for user-related operations including sign-up, deletion, retrieval,
 * and administrative batch operations.
 */
public interface UserService {

    /**
     * Registers a new user with the provided request and role.
     *
     * @param request the signup request containing username and password
     * @param role    the role to assign to the user
     * @return a response containing the user ID, username, and success message
     */
    SignupResponse signup(SignupRequest request, Role role);

    /**
     * Deletes a user by their ID.
     *
     * @param userId         the UUID of the user to delete
     * @param adminDeletion  if true, triggers an admin notification
     * @throws com.william.puzzle.exception.user.UserNotFoundException if user not found
     */
    void deleteUser(UUID userId, boolean adminDeletion);

    /**
     * Retrieves a specific user's administrative details.
     *
     * @param userId the UUID of the user
     * @return the user details formatted for admin viewing
     * @throws com.william.puzzle.exception.user.UserNotFoundException if user not found
     */
    UserAdminResponse getUser(UUID userId);

    /**
     * Retrieves all registered users in an admin-friendly format.
     *
     * @return a list of all users
     */
    List<UserAdminResponse> getAllUsers();

    /**
     * Deletes all users who haven't logged in since the given cutoff time.
     * Also deletes related game data and notifies admins.
     *
     * @param cutoffTime the cutoff login time
     * @return a response containing the count of deleted users
     */
    MassDeletionResponse deleteUsersNotLoggedInSinceDate(LocalDateTime cutoffTime);
}
