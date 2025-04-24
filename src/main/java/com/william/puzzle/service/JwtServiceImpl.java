package com.william.puzzle.service;

import com.william.puzzle.constants.LogMessages;
import com.william.puzzle.exception.auth.InvalidJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService {

    private final SecretKey signingKey;
    private static final String TOKEN_TYPE = "tokenType";
    private static final String ACCESS = "access";
    private static final String REFRESH = "refresh";

    @Value("${jwt.access-token-duration}")
    private Long accessTokenDuration;

    @Value("${jwt.refresh-token-duration}")
    private Long refreshTokenDuration;

    public long extractExpiration(String token) {
        return extractClaims(token).getExpiration().getTime();
    }

    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException ex) {
            log.warn(LogMessages.INVALID_JWT_WITH_REASON, ex.getMessage());
            throw new InvalidJwtException(LogMessages.INVALID_JWT, ex);
        }
    }

    public Claims validateAndExtractClaims(String token) {
        var claims = extractClaims(token);
        if (isTokenExpired(claims)) {
            throw new InvalidJwtException("JWT token has expired");
        }
        return claims;
    }

    public Optional<String> getBearerToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
                .filter(auth -> auth.startsWith("Bearer "))
                .map(auth -> auth.substring(7))
                .or(() -> {
                    log.warn("Missing or invalid Authorization header");
                    return Optional.empty();
                });
    }




    public String createAccessJwt(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_TYPE, ACCESS);
        return createJwt(username, accessTokenDuration, claims);
    }

    public String createRefreshJwt(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_TYPE, REFRESH);
        return createJwt(username, refreshTokenDuration, claims);
    }

    public boolean isRefreshToken(Claims claims) {
        return REFRESH.equals(claims.get(TOKEN_TYPE));
    }

    private String createJwt(String username, Long duration, Map<String, Object> claims) {
        var currentMillisecond = System.currentTimeMillis();

        return Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(new Date(currentMillisecond))
                .expiration(new Date(currentMillisecond + duration))
                .signWith(signingKey)
                .compact();
    }
}
