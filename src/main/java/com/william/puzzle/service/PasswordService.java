package com.william.puzzle.service;

/**
 * Service interface for handling password encoding and verification.
 */
public interface PasswordService {

    /**
     * Encodes a raw password using a secure hashing algorithm.
     *
     * @param password the plain-text password to encode
     * @return the encoded (hashed) version of the password
     */
    String encode(String password);

    /**
     * Verifies a password attempt against a stored encoded password.
     *
     * @param passwordAttempt the raw password input to verify
     * @param encodedPassword the previously stored encoded password
     * @return true if the password attempt matches the encoded password, false otherwise
     */
    boolean matches(String passwordAttempt, String encodedPassword);
}
