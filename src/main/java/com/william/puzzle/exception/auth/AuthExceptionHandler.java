package com.william.puzzle.exception.auth;

import com.william.puzzle.constants.ApiPaths;
import com.william.puzzle.constants.ProblemTypeURIs;
import com.william.puzzle.exception.user.ExistingUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.william.puzzle.exception.util.ProblemDetailBuilder.buildProblem;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(ExistingUserException.class)
    public ResponseEntity<ProblemDetail> existingUserHandler(ExistingUserException exception) {
        var problem = buildProblem(HttpStatus.CONFLICT, exception.getMessage(), ProblemTypeURIs.CONFLICT, ApiPaths.SIGNUP);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<ProblemDetail> invalidLoginHandler(InvalidLoginException exception) {
        var problem = buildProblem(HttpStatus.UNAUTHORIZED, exception.getMessage(), ProblemTypeURIs.UNAUTHORIZED, ApiPaths.LOGIN);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<ProblemDetail> invalidJwtHandler(InvalidJwtException exception) {
        var problem = buildProblem(HttpStatus.UNAUTHORIZED, exception.getMessage(), ProblemTypeURIs.UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }
}
