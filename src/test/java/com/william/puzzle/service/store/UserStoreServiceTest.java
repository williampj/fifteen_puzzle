package com.william.puzzle.service.store;

import com.william.puzzle.enums.Role;
import com.william.puzzle.exception.user.ExistingUserException;
import com.william.puzzle.model.User;
import com.william.puzzle.service.PasswordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.anyString;

class UserStoreServiceTest {

    private PasswordServiceImpl passwordService;
    private UserStoreServiceImpl userStoreService;

    @BeforeEach
    void setUp() {
        passwordService = mock(PasswordServiceImpl.class);
        userStoreService = new UserStoreServiceImpl(passwordService);
    }

    @Test
    void addUser_ShouldAddUserSuccessfully() {
        String username = "testuser";
        String password = "testpassword";
        when(passwordService.encode(password)).thenReturn("encodedPassword");

        UUID userId = userStoreService.addUser(username, password, Role.PLAYER);

        User storedUser = userStoreService.getUser(userId);
        assertNotNull(storedUser);
        assertEquals(username, storedUser.getUsername());
        assertEquals("encodedPassword", storedUser.getEncodedPassword());
        assertEquals(Role.PLAYER, storedUser.getRole());
    }

    @Test
    void addUser_ShouldThrowException_WhenUserAlreadyExists() {
        String username = "duplicate";
        String password = "pw";
        when(passwordService.encode(password)).thenReturn("encoded");

        userStoreService.addUser(username, password, Role.ADMIN);

        assertThrows(ExistingUserException.class, () ->
                userStoreService.addUser(username, password, Role.ADMIN)
        );
    }

    @Test
    void getUser_WithCorrectPassword_ShouldReturnUser() {
        String username = "user";
        String encodedPassword = "encoded123";
        String attempt = "raw123";

        when(passwordService.encode(anyString())).thenReturn(encodedPassword);
        when(passwordService.matches(attempt, encodedPassword)).thenReturn(true);

        UUID userId = userStoreService.addUser(username, "initial", Role.PLAYER);
        User stored = userStoreService.getUser(userId);
        stored.setEncodedPassword(encodedPassword);

        User result = userStoreService.getUser(username, attempt);

        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    void getUser_WithIncorrectPassword_ShouldReturnNull() {
        String username = "wrong";
        String encoded = "encoded";
        when(passwordService.encode(anyString())).thenReturn(encoded);
        when(passwordService.matches("wrongAttempt", encoded)).thenReturn(false);

        UUID userId = userStoreService.addUser(username, "pass", Role.PLAYER);
        User stored = userStoreService.getUser(userId);
        stored.setEncodedPassword(encoded);

        User result = userStoreService.getUser(username, "wrongAttempt");

        assertNull(result);
    }

    @Test
    void getUser_ByUsername_ReturnsCorrectUser() {
        String username = "findme";
        UUID userId = userStoreService.addUser(username, "pw", Role.ADMIN);

        User result = userStoreService.getUser(username);

        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    void deleteUser_RemovesUserSuccessfully() {
        UUID userId = userStoreService.addUser("todelete", "pw", Role.PLAYER);

        boolean deleted = userStoreService.deleteUser(userId);

        assertTrue(deleted);
        assertNull(userStoreService.getUser(userId));
    }

    @Test
    void deleteUser_ReturnsFalse_WhenUserNotFound() {
        UUID fakeId = UUID.randomUUID();

        boolean deleted = userStoreService.deleteUser(fakeId);

        assertFalse(deleted);
    }

    @Test
    void getAllUsers_ReturnsOnlyPlayers() {
        userStoreService.addUser("player1", "pw1", Role.PLAYER);
        userStoreService.addUser("admin1", "pw2", Role.ADMIN);
        userStoreService.addUser("player2", "pw3", Role.PLAYER);

        List<User> players = userStoreService.getAllUsers();

        assertEquals(2, players.size());
        assertTrue(players.stream().allMatch(u -> u.getRole() == Role.PLAYER));
    }

    @Test
    void deleteUsersNotLoggedInSinceDate_RemovesInactiveUsers() {
        String activeUser = "active";
        String inactiveUser = "inactive";

        UUID activeId = userStoreService.addUser(activeUser, "pw", Role.PLAYER);
        UUID inactiveId = userStoreService.addUser(inactiveUser, "pw", Role.PLAYER);

        User active = userStoreService.getUser(activeId);
        User inactive = userStoreService.getUser(inactiveId);

        active.setLastLogin(LocalDateTime.now());
        inactive.setLastLogin(LocalDateTime.now().minusDays(10));

        List<UUID> removed = userStoreService.deleteUsersNotLoggedInSinceDate(LocalDateTime.now().minusDays(5));

        assertEquals(1, removed.size());
        assertEquals(inactiveId, removed.get(0));
        assertNull(userStoreService.getUser(inactiveId));
    }

    @Test
    void getUserName_ReturnsCorrectUsername() {
        UUID userId = userStoreService.addUser("checkname", "pw", Role.PLAYER);

        String username = userStoreService.getUserName(userId);

        assertEquals("checkname", username);
    }
}
