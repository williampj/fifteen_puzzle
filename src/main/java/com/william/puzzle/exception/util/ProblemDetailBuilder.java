package com.william.puzzle.exception.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

public class ProblemDetailBuilder {
    public static ProblemDetail buildProblem(HttpStatus status, String detail, String type, String instance) {
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setDetail(detail);
        problem.setType(URI.create(type));
        problem.setInstance(URI.create(instance));
        return problem;
    }

    public static ProblemDetail buildProblem(HttpStatus status, String detail, String type) {
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setDetail(detail);
        problem.setType(URI.create(type));
        return problem;
    }
}



