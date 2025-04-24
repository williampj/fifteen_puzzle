package com.william.puzzle.service;

import com.william.puzzle.exception.auth.InvalidJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private JwtService jwtService;
    private SecretKey secretKey;

    private static final long ACCESS_DURATION = 3600000; // 1 hour
    private static final long REFRESH_DURATION = 86400000; // 24 hours

    @BeforeEach
    void setUp() throws Exception {
        secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        jwtService = new JwtServiceImpl(secretKey);

        setField(jwtService, "accessTokenDuration", ACCESS_DURATION);
        setField(jwtService, "refreshTokenDuration", REFRESH_DURATION);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void createAndValidateAccessToken_shouldSucceed() {
        String username = "testUser";
        String token = jwtService.createAccessJwt(username);

        Claims claims = jwtService.validateAndExtractClaims(token);

        assertEquals(username, claims.getSubject());
        assertEquals("access", claims.get("tokenType"));
    }

    @Test
    void createAndValidateRefreshToken_shouldSucceed() {
        String username = "testUser";
        String token = jwtService.createRefreshJwt(username);

        Claims claims = jwtService.validateAndExtractClaims(token);

        assertEquals(username, claims.getSubject());
        assertTrue(jwtService.isRefreshToken(claims));
    }

    @Test
    void extractClaims_withInvalidToken_shouldThrowException() {
        String invalidToken = "this.is.not.a.valid.token";

        assertThrows(InvalidJwtException.class, () -> jwtService.extractClaims(invalidToken));
    }

    @Test
    void isTokenExpired_shouldReturnTrueForExpiredToken() {
        Date now = new Date();
        Claims expiredClaims = mock(Claims.class);
        when(expiredClaims.getExpiration()).thenReturn(new Date(now.getTime() - 10000));

        assertTrue(jwtService.isTokenExpired(expiredClaims));
    }

    @Test
    void isTokenExpired_shouldReturnFalseForValidToken() {
        Date now = new Date();
        Claims validClaims = mock(Claims.class);
        when(validClaims.getExpiration()).thenReturn(new Date(now.getTime() + 10000));

        assertFalse(jwtService.isTokenExpired(validClaims));
    }

    @Test
    void getBearerToken_shouldReturnTokenIfHeaderValid() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer sometoken");

        Optional<String> token = jwtService.getBearerToken(request);

        assertTrue(token.isPresent());
        assertEquals("sometoken", token.get());
    }

    @Test
    void getBearerToken_shouldReturnEmptyIfHeaderInvalid() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Invalid header");

        Optional<String> token = jwtService.getBearerToken(request);

        assertTrue(token.isEmpty());
    }
}
