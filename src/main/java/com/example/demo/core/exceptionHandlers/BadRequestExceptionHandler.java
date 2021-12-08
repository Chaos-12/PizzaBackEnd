package com.example.demo.core.exceptionHandlers;

import com.example.demo.core.exceptions.BadRequestException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BadRequestExceptionHandler {
    private final Logger logger;

    @Autowired
    public BadRequestExceptionHandler(final Logger logger) {
        this.logger = logger;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleConflict(BadRequestException ex) {
        logger.warn(String.format("%s , StackTrace: %s", ex.getMessage(), ex.getStackTrace().toString()));
        return ResponseEntity.status(ex.getCode()).body(ex.getExceptions());
    }
}