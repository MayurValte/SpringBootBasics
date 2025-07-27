package com.mvctest.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<APIError> handleResourceNotFoundException(NoSuchElementException exception) {
        APIError apiError = APIError.builder().httpStatus(HttpStatus.NOT_FOUND).message("Resource not fount").build();
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
}
