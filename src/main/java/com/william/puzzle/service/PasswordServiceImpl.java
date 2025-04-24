package com.william.puzzle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PasswordServiceImpl implements PasswordService {
    private final BCryptPasswordEncoder passwordEncoder;

    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean matches(String passwordAttempt, String encodedPassword) {
        return passwordEncoder.matches(passwordAttempt, encodedPassword);
    }
}
