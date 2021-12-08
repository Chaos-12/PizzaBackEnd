package com.example.demo.core.exceptionHandlers;

import com.example.demo.core.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NotFoundExceptionHandler {
    private final Logger logger;

    @Autowired
    public NotFoundExceptionHandler(final Logger logger){
        this.logger = logger;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleConflict(NotFoundException ex) {
        logger.warn(String.format("%s , StackTrace: %s", ex.getMessage(), ex.getStackTrace().toString()));
        return ResponseEntity.status(ex.getCode()).body(ex.getMessage());
    }
}