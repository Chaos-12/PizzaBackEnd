package com.example.demo.core.exceptionHandlers;

import com.example.demo.core.exceptions.RedisConnectionException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RedisConnectionExceptionHandler {
    private final Logger logger;
    
    @Autowired
    public RedisConnectionExceptionHandler(final Logger logger){
      this.logger = logger;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RedisConnectionException.class)
    protected ResponseEntity<Object> handleRedisConnectionConflict(RedisConnectionException ex) {
        logger.warn(String.format("%s , StackTrace: %s", ex.getMessage(), ex.getStackTrace().toString()));
        return ResponseEntity.status(ex.getCode()).body(ex.getMessage());
    }    
}