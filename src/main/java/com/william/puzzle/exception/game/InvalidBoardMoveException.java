package com.william.puzzle.exception.game;

public class InvalidBoardMoveException extends RuntimeException{
    public InvalidBoardMoveException(String message) {
        super(message);
    }
}
