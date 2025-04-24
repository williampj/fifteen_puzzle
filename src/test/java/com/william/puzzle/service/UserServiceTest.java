package com.william.puzzle.service;

import com.william.puzzle.constants.ResponseMessages;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserStoreServiceImpl userStoreService;
    private GameStoreServiceImpl gameStoreService;
    private AdminNotificationPublisherImpl adminNotificationPublisher;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userStoreService = mock(UserStoreServiceImpl.class);
        gameStoreService = mock(GameStoreServiceImpl.class);
        adminNotificationPublisher = mock(AdminNotificationPublisherImpl.class);
        userService = new UserServiceImpl(userStoreService, gameStoreService, adminNotificationPublisher);
    }

    @Test
    void signup_shouldReturnCorrectResponse() {
        var request = new SignupRequest("testuser", "securePassword123");
        var expectedId = UUID.randomUUID();

        when(userStoreService.addUser("testuser", "securePassword123", Role.PLAYER)).thenReturn(expectedId);

        SignupResponse response = userService.signup(request, Role.PLAYER);

        assertEquals(expectedId, response.userId());
        assertEquals("testuser", response.username());
        assertEquals(ResponseMessages.SUCCESSFUL_SIGNUP, response.message());
    }

    @Test
    void deleteUser_shouldThrowIfUserDoesNotExist() {
        var userId = UUID.randomUUID();
        when(userStoreService.deleteUser(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId, false));
    }

    @Test
    void deleteUser_shouldNotifyAdminWhenFlagged() {
        var userId = UUID.randomUUID();

        when(userStoreService.deleteUser(userId)).thenReturn(true);
        when(userStoreService.getUserName(userId)).thenReturn("testuser");

        userService.deleteUser(userId, true);

        verify(adminNotificationPublisher, times(1)).sendUserDeletedNotification(argThat(notification ->
                notification.userId().equals(userId)
                        && notification.type() == AdminNotificationType.USER_DELETED
                        && notification.message().contains("testuser")));
    }

    @Test
    void getUser_shouldReturnMappedResponse() {
        var userId = UUID.randomUUID();
        var user = new User(userId, "jane", "pass", Role.PLAYER, LocalDateTime.now());

        when(userStoreService.getUser(userId)).thenReturn(user);

        UserAdminResponse response = userService.getUser(userId);

        assertEquals("jane", response.username());
        assertEquals(userId, response.id());
    }

    @Test
    void getAllUsers_shouldMapCorrectly() {
        var user1 = new User(UUID.randomUUID(), "user1", "pass", Role.PLAYER);
        var user2 = new User(UUID.randomUUID(), "user2", "pass", Role.PLAYER);

        when(userStoreService.getAllUsers()).thenReturn(List.of(user1, user2));

        var result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).username());
    }

    @Test
    void deleteUsersNotLoggedInSinceDate_shouldDeleteAndNotify() {
        var userId1 = UUID.randomUUID();
        var userId2 = UUID.randomUUID();
        var now = LocalDateTime.now().minusDays(30);

        when(userStoreService.deleteUsersNotLoggedInSinceDate(now)).thenReturn(List.of(userId1, userId2));
        when(userStoreService.getUserName(userId1)).thenReturn("User1");
        when(userStoreService.getUserName(userId2)).thenReturn("User2");

        MassDeletionResponse response = userService.deleteUsersNotLoggedInSinceDate(now);

        verify(gameStoreService).deleteGamesByUser(userId1);
        verify(gameStoreService).deleteGamesByUser(userId2);
        verify(adminNotificationPublisher, times(2)).sendUserDeletedNotification(any());
        assertEquals(2, response.deletionCount());
    }
}
