package com.william.puzzle.exception.auth;

public class InvalidJwtException extends RuntimeException{
    public InvalidJwtException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidJwtException(String message) {
        super(message);
    }
}
