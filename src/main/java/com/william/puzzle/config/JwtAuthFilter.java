package com.william.puzzle.config;

import com.william.puzzle.constants.ApiPaths;
import com.william.puzzle.exception.auth.InvalidJwtException;
import com.william.puzzle.model.User;
import com.william.puzzle.service.JwtBlacklistServiceImpl;
import com.william.puzzle.service.JwtServiceImpl;
import com.william.puzzle.service.store.UserStoreServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtServiceImpl jwtService;
    private final UserStoreServiceImpl userStoreService;
    private final JwtBlacklistServiceImpl jwtBlacklistService;

    private static final List<String> WHITELISTED_PATHS = List.of(ApiPaths.LOGIN, ApiPaths.SIGNUP, ApiPaths.REFRESH);

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return WHITELISTED_PATHS.stream().anyMatch((uri::startsWith));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Incoming request to: {}", request.getRequestURI());
        var token = jwtService.getBearerToken(request);
        if (token.isEmpty()) {
            log.debug("No Bearer token found");
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtBlacklistService.isBlacklisted(token.get())) {
            log.debug("The token has been invalidated");
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            log.trace("The user is already authenticarted");
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims;
        try {
            claims = jwtService.validateAndExtractClaims(token.get());
        } catch (InvalidJwtException ex) {
            log.warn("Invalid JWT: {}", ex.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        var username = claims.getSubject();
        if (username == null) {
            log.warn("JWT is missing a username");
            filterChain.doFilter(request, response);
            return;
        }

        var user = userStoreService.getUser(username);
        if (user == null) {
            log.warn("User '{}' not found", username); // TODO move to constant
            filterChain.doFilter(request, response);
            return;
        }

        setAuthentication(user, request);
        log.debug("Authenticated user '{}'", username); // TODO remove before "prod"
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(User user, HttpServletRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
