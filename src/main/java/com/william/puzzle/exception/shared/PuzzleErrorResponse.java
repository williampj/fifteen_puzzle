package com.william.puzzle.exception.shared;

import lombok.Builder;
import lombok.Getter;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

@Builder
@Getter
public class PuzzleErrorResponse implements ErrorResponse {
    private final HttpStatusCode status;
    private final ProblemDetail body;

    @Override
    public HttpStatusCode getStatusCode() {
        return status;
    }

    @Override
    public ProblemDetail getBody() {
        return body;
    }
}
