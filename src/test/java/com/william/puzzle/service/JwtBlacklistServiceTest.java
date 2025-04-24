package com.william.puzzle.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtBlacklistServiceTest {

    private JwtBlacklistServiceImpl jwtBlacklistService;

    @BeforeEach
    void setUp() {
        jwtBlacklistService = new JwtBlacklistServiceImpl();
    }

    @Test
    void isBlacklisted_ReturnsFalse_WhenTokenNotInBlacklist() {
        String token = "some.jwt.token";

        boolean result = jwtBlacklistService.isBlacklisted(token);

        assertFalse(result);
    }

    @Test
    void addToBlacklist_MakesTokenBlacklisted() {
        String token = "blacklisted.jwt.token";

        jwtBlacklistService.addToBlacklist(token);

        assertTrue(jwtBlacklistService.isBlacklisted(token));
    }
}
