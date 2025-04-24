package com.william.puzzle.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

/**
 * Service interface for handling JWT operations such as creation, validation,
 * and claim extraction.
 */
public interface JwtService {

    /**
     * Extracts the expiration time from a JWT token.
     *
     * @param token the JWT token
     * @return expiration time in milliseconds since epoch
     */
    long extractExpiration(String token);

    /**
     * Checks if the provided JWT claims are expired.
     *
     * @param claims the JWT claims
     * @return {@code true} if the token is expired, {@code false} otherwise
     */
    boolean isTokenExpired(Claims claims);

    /**
     * Extracts claims from a JWT token. May throw if the token is invalid.
     *
     * @param token the JWT token
     * @return the extracted claims
     */
    Claims extractClaims(String token);

    /**
     * Extracts claims from a JWT and ensures the token is not expired.
     *
     * @param token the JWT token
     * @return valid claims
     * @throws com.william.puzzle.exception.auth.InvalidJwtException if the token is invalid or expired
     */
    Claims validateAndExtractClaims(String token);

    /**
     * Retrieves the Bearer token from the Authorization header of an HTTP request.
     *
     * @param request the HTTP request
     * @return an optional containing the token if present and valid
     */
    Optional<String> getBearerToken(HttpServletRequest request);

    /**
     * Creates a new JWT access token for a given username.
     *
     * @param username the username to include in the token
     * @return the generated JWT access token
     */
    String createAccessJwt(String username);

    /**
     * Creates a new JWT refresh token for a given username.
     *
     * @param username the username to include in the token
     * @return the generated JWT refresh token
     */
    String createRefreshJwt(String username);

    /**
     * Determines if the given claims represent a refresh token.
     *
     * @param claims the JWT claims
     * @return {@code true} if the token is a refresh token, {@code false} otherwise
     */
    boolean isRefreshToken(Claims claims);
}
