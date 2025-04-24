package com.william.puzzle.service;

import com.william.puzzle.constants.ExceptionMessages;
import com.william.puzzle.dto.LoginResponse;
import com.william.puzzle.dto.LoginRequest;
import com.william.puzzle.dto.RefreshJwtResponse;
import com.william.puzzle.exception.auth.InvalidJwtException;
import com.william.puzzle.exception.auth.InvalidLoginException;
import com.william.puzzle.service.store.UserStoreServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserStoreServiceImpl userStoreService;
    private final JwtServiceImpl jwtService;
    private final JwtBlacklistServiceImpl jwtBlacklistService;

    public LoginResponse login(LoginRequest request) {
        var user = this.userStoreService.getUser(request.username(), request.password());
        if (user == null) {
            throw new InvalidLoginException();
        }
        user.setLastLogin(LocalDateTime.now());
        var accessJwt = jwtService.createAccessJwt(user.getUsername());
        var expiration = jwtService.extractExpiration(accessJwt);
        var refreshJwt = jwtService.createRefreshJwt(user.getUsername());

        return new LoginResponse(accessJwt, refreshJwt, expiration, user.getUsername(), user.getRole().name());
    }

    public void logout(String accessToken, String refreshToken) {
        jwtBlacklistService.addToBlacklist(accessToken);
        jwtBlacklistService.addToBlacklist(refreshToken);
    }

    public RefreshJwtResponse refreshToken(String refreshToken) {
        var claims = jwtService.validateAndExtractClaims(refreshToken);
        if (!jwtService.isRefreshToken(claims)) {
            throw new InvalidJwtException(ExceptionMessages.INVALID_REFRESH_TOKEN);
        }
        var username = claims.getSubject();
        var accessToken = jwtService.createAccessJwt(username);
        var expiresAt = jwtService.extractExpiration(accessToken);

        return new RefreshJwtResponse(accessToken, expiresAt);
    }
}
