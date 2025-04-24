package com.william.puzzle.exception.auth;

import com.william.puzzle.constants.ExceptionMessages;

public class InvalidLoginException extends RuntimeException{

    public InvalidLoginException() {
        super(ExceptionMessages.INVALID_LOGIN);
    }
}