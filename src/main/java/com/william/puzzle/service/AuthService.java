package com.william.puzzle.service;

import com.william.puzzle.dto.LoginRequest;
import com.william.puzzle.dto.LoginResponse;
import com.william.puzzle.dto.RefreshJwtResponse;

/**
 * Interface defining authentication operations such as login, logout,
 * and token refreshing.
 */
public interface AuthService {

    /**
     * Authenticates a user using the provided login request.
     *
     * @param request the login request containing username and password
     * @return a response containing access and refresh tokens, expiration info, and user details
     * @throws com.william.puzzle.exception.auth.InvalidLoginException if authentication fails
     */
    LoginResponse login(LoginRequest request);

    /**
     * Logs out a user by blacklisting the provided access and refresh tokens.
     *
     * @param accessToken  the JWT used for authorization
     * @param refreshToken the JWT used to refresh the session
     */
    void logout(String accessToken, String refreshToken);

    /**
     * Refreshes the access token using a valid refresh token.
     *
     * @param refreshToken the refresh token used to generate a new access token
     * @return a response containing the new access token and its expiration time
     * @throws com.william.puzzle.exception.auth.InvalidJwtException if the token is invalid or not a refresh token
     */
    RefreshJwtResponse refreshToken(String refreshToken);
}
