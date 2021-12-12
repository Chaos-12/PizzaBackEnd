package com.example.demo.core.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends HttpException {

    public NotFoundException() {
        this("Not found exception");
    }
    
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND.value(), message);
    }
}