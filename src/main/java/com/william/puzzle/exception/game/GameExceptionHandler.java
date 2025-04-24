package com.william.puzzle.exception.game;

import com.william.puzzle.constants.ProblemTypeURIs;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.william.puzzle.exception.util.ProblemDetailBuilder.buildProblem;

@RestControllerAdvice
public class GameExceptionHandler {

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ProblemDetail> gameNotFoundHandler(GameNotFoundException exception) {
        var problem = buildProblem(HttpStatus.NOT_FOUND, exception.getMessage(), ProblemTypeURIs.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(InvalidBoardMoveException.class)
    public ResponseEntity<ProblemDetail> InvalidBoardMoveException(InvalidBoardMoveException exception) {
        var problem = buildProblem(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage(), ProblemTypeURIs.UNPROCESSABLE_ENTITY);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(problem);
    }
}
