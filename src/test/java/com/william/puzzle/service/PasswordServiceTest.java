package com.william.puzzle.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordServiceTest {

    private BCryptPasswordEncoder passwordEncoder;
    private PasswordServiceImpl passwordService;

    @BeforeEach
    void setUp() {
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        passwordService = new PasswordServiceImpl(passwordEncoder);
    }

    @Test
    void encode_ReturnsEncodedPassword() {
        String rawPassword = "mySecretPassword";
        String encoded = "$2a$10$abc123encoded";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encoded);

        String result = passwordService.encode(rawPassword);

        assertEquals(encoded, result);
        verify(passwordEncoder).encode(rawPassword);
    }

    @Test
    void matches_ReturnsTrue_WhenPasswordsMatch() {
        String raw = "password123";
        String encoded = "$2a$10$somehash";

        when(passwordEncoder.matches(raw, encoded)).thenReturn(true);

        assertTrue(passwordService.matches(raw, encoded));
        verify(passwordEncoder).matches(raw, encoded);
    }

    @Test
    void matches_ReturnsFalse_WhenPasswordsDoNotMatch() {
        String raw = "wrongPassword";
        String encoded = "$2a$10$correcthash";

        when(passwordEncoder.matches(raw, encoded)).thenReturn(false);

        assertFalse(passwordService.matches(raw, encoded));
        verify(passwordEncoder).matches(raw, encoded);
    }
}
