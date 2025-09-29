package com.solutions.interview.exceptionHandling;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(BusinessException ex) {
        ApiError apiError = new ApiError(
                ex.getStatus().value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(ex.getStatus()).body(apiError);
    }

}
