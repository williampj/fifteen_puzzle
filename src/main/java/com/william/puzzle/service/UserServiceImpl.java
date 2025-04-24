package com.william.puzzle.service;

import com.william.puzzle.constants.ExceptionMessages;
import com.william.puzzle.constants.ResponseMessages;
import com.william.puzzle.dto.AdminNotification;
import com.william.puzzle.dto.MassDeletionResponse;
import com.william.puzzle.dto.SignupRequest;
import com.william.puzzle.dto.SignupResponse;
import com.william.puzzle.dto.UserAdminResponse;
import com.william.puzzle.enums.AdminNotificationType;
import com.william.puzzle.enums.Role;
import com.william.puzzle.exception.user.UserNotFoundException;
import com.william.puzzle.model.User;
import com.william.puzzle.service.store.GameStoreServiceImpl;
import com.william.puzzle.service.store.UserStoreServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserStoreServiceImpl userStoreService;
    private final GameStoreServiceImpl gameStoreService;
    private final AdminNotificationPublisherImpl adminNotificationPublisher;

    public SignupResponse signup(SignupRequest request, Role role) {
        var username = request.username();
        var userId = this.userStoreService.addUser(username, request.password(), role);

        return new SignupResponse(userId, username, ResponseMessages.SUCCESSFUL_SIGNUP);
    }

    public void deleteUser(UUID userId, boolean adminDeletion) {
        var deletion = userStoreService.deleteUser(userId);
        if (!deletion) {
            throw new UserNotFoundException(ExceptionMessages.USERID_NOT_FOUND + ": " + userId);
        }
        gameStoreService.getGamesByUser(userId);
        if (adminDeletion) {
            notifyDeletedUser(userId);
        }
    }

    public UserAdminResponse getUser(UUID userId) {
        var user = userStoreService.getUser(userId);
        return mapToUserAdminResponse(user);
    }

    public List<UserAdminResponse> getAllUsers() {
        var users = userStoreService.getAllUsers();
        return users.stream().map(this::mapToUserAdminResponse).toList();
    }

    public MassDeletionResponse deleteUsersNotLoggedInSinceDate(LocalDateTime cutoffTime) {
        var deletedUserIds = userStoreService.deleteUsersNotLoggedInSinceDate(cutoffTime);

        deletedUserIds.forEach(userId -> {
            gameStoreService.deleteGamesByUser(userId);
            notifyDeletedUser(userId);
        });

        return new MassDeletionResponse(deletedUserIds.size());
    }

    private void notifyDeletedUser(UUID userId) {
        var userName = userStoreService.getUserName(userId);
        var type = AdminNotificationType.USER_DELETED;
        var message = type.format(userName);
        var notification = new AdminNotification(userId, type, message);
        adminNotificationPublisher.sendUserDeletedNotification(notification);
    }

    private UserAdminResponse mapToUserAdminResponse(User user) {
        return new UserAdminResponse(
                user.getId(),
                user.getUsername(),
                user.getLastLogin()
        );
    }
}