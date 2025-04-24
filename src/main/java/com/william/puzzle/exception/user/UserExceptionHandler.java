package com.william.puzzle.exception.user;

import com.william.puzzle.constants.ApiPaths;
import com.william.puzzle.constants.ProblemTypeURIs;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.william.puzzle.exception.util.ProblemDetailBuilder.buildProblem;

@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(ExistingUserException.class)
    public ResponseEntity<ProblemDetail> existingUserHandler(ExistingUserException exception) {
        var problem = buildProblem(HttpStatus.CONFLICT, exception.getMessage(), ProblemTypeURIs.CONFLICT, ApiPaths.SIGNUP);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> userNotFoundHandler(UserNotFoundException exception) {
        var problem = buildProblem(HttpStatus.NOT_FOUND, exception.getMessage(), ProblemTypeURIs.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }
}
