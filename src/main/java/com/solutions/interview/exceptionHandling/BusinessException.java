package com.solutions.interview.exceptionHandling;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
    private final HttpStatus status;

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public BusinessException(String message) {
        this(message, HttpStatus.BAD_REQUEST); // default to 400
    }

    public HttpStatus getStatus() {
        return status;
    }
}
