package com.william.puzzle.dto;

public record LogoutRequest(String accessToken, String refreshToken) { }
