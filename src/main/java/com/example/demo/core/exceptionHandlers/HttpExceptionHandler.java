package com.example.demo.core.exceptionHandlers;

import com.example.demo.core.exceptions.HttpException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class HttpExceptionHandler {
    @ExceptionHandler(HttpException.class)
    protected ResponseEntity<Object> handleConflict(HttpException ex) {
        log.warn(String.format("%s , StackTrace: %s", ex.getMessage(), ex.getStackTrace().toString()));
        return ResponseEntity.status(ex.getCode()).body(ex.getMessage());
    }
}