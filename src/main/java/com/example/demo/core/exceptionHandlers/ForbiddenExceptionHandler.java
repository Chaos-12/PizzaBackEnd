package com.example.demo.core.exceptionHandlers;

import com.example.demo.core.exceptions.ForbiddenException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ForbiddenExceptionHandler {
    private final Logger logger;

    @Autowired
    public ForbiddenExceptionHandler(final Logger logger) {
        this.logger = logger;
    }

    @ExceptionHandler(value = { ForbiddenException.class })
    protected ResponseEntity<Object> handleConflict(ForbiddenException ex, WebRequest request) {
        logger.warn(String.format("%s , StackTrace: %s", ex.getMessage(), ex.getStackTrace().toString()));
        return ResponseEntity.status(ex.getCode()).body(ex.getExceptions());
    }
}