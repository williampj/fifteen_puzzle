package com.william.puzzle.service;

/**
 * Interface for managing blacklisted JWT tokens to prevent reuse after logout or invalidation.
 */
public interface JwtBlacklistService {

    /**
     * Checks if a given JWT token is blacklisted.
     *
     * @param token the JWT token to check
     * @return true if the token is blacklisted, false otherwise
     */
    boolean isBlacklisted(String token);

    /**
     * Adds a JWT token to the blacklist.
     *
     * @param token the JWT token to blacklist
     */
    void addToBlacklist(String token);
}
