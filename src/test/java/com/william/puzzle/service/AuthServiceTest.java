package com.william.puzzle.service;

import com.william.puzzle.dto.LoginRequest;
import com.william.puzzle.dto.LoginResponse;
import com.william.puzzle.dto.RefreshJwtResponse;
import com.william.puzzle.enums.Role;
import com.william.puzzle.exception.auth.InvalidJwtException;
import com.william.puzzle.exception.auth.InvalidLoginException;
import com.william.puzzle.model.User;
import com.william.puzzle.service.store.UserStoreServiceImpl;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserStoreServiceImpl userStoreService;

    @Mock
    private JwtServiceImpl jwtService;

    @Mock
    private JwtBlacklistServiceImpl jwtBlacklistService;

    @InjectMocks
    private AuthServiceImpl authService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() {
        String username = "testuser";
        String password = "securePassword!";
        String accessToken = "access.jwt.token";
        String refreshToken = "refresh.jwt.token";
        long expiresAt = System.currentTimeMillis() + 3600000;

        User user = new User(UUID.randomUUID(), username, "encodedPassword", Role.PLAYER);
        when(userStoreService.getUser(username, password)).thenReturn(user);
        when(jwtService.createAccessJwt(username)).thenReturn(accessToken);
        when(jwtService.extractExpiration(accessToken)).thenReturn(expiresAt);
        when(jwtService.createRefreshJwt(username)).thenReturn(refreshToken);

        LoginResponse response = authService.login(new LoginRequest(username, password));

        assertNotNull(response);
        assertEquals(username, response.username());
        assertEquals("PLAYER", response.role());
        assertEquals(accessToken, response.accessToken());
        assertEquals(refreshToken, response.refreshToken());
        assertEquals(expiresAt, response.expiresAt());

        verify(userStoreService).getUser(username, password);
        verify(jwtService).createAccessJwt(username);
        verify(jwtService).createRefreshJwt(username);
    }

    @Test
    void testLogin_InvalidUser_ThrowsException() {
        when(userStoreService.getUser("invalid", "wrong")).thenReturn(null);

        assertThrows(InvalidLoginException.class, () -> {
            authService.login(new LoginRequest("invalid", "wrong"));
        });

        verify(jwtService, never()).createAccessJwt(any());
        verify(jwtService, never()).createRefreshJwt(any());
    }

    @Test
    void testLogout_AddsTokensToBlacklist() {
        String access = "access.token";
        String refresh = "refresh.token";

        authService.logout(access, refresh);

        verify(jwtBlacklistService).addToBlacklist(access);
        verify(jwtBlacklistService).addToBlacklist(refresh);
    }

    @Test
    void testRefreshToken_Success() {
        String username = "testuser";
        String refreshToken = "refresh.jwt.token";
        String newAccessToken = "new.access.token";
        long expiresAt = System.currentTimeMillis() + 3600000;

        Claims claims = mock(Claims.class);
        when(jwtService.validateAndExtractClaims(refreshToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(username);
        when(jwtService.isRefreshToken(claims)).thenReturn(true);
        when(jwtService.createAccessJwt(username)).thenReturn(newAccessToken);
        when(jwtService.extractExpiration(newAccessToken)).thenReturn(expiresAt);

        RefreshJwtResponse response = authService.refreshToken(refreshToken);

        assertNotNull(response);
        assertEquals(newAccessToken, response.accessToken());
        assertEquals(expiresAt, response.expiresAt());
    }

    @Test
    void testRefreshToken_InvalidTokenType_ThrowsException() {
        Claims claims = mock(Claims.class);
        when(jwtService.validateAndExtractClaims("bad.token")).thenReturn(claims);
        when(jwtService.isRefreshToken(claims)).thenReturn(false);

        assertThrows(InvalidJwtException.class, () -> {
            authService.refreshToken("bad.token");
        });

        verify(jwtService, never()).createAccessJwt(any());
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
