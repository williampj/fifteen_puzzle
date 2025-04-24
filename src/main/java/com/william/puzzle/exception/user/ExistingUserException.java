package com.william.puzzle.exception.user;

import com.william.puzzle.constants.ExceptionMessages;

public class ExistingUserException extends RuntimeException {

    public ExistingUserException(String username) {
        super(ExceptionMessages.EXISTING_USER + ": " + username);
    }
}
