package com.william.puzzle.service;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtBlacklistServiceImpl implements JwtBlacklistService {
    private final Map<String, Date> blacklistedJwts = new ConcurrentHashMap<>();

    public boolean isBlacklisted(String token) {
        return blacklistedJwts.containsKey(token);
    }

    public void addToBlacklist(String token) {
        blacklistedJwts.put(token, new Date());
    }
}