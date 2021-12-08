package com.example.demo.core.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends HttpException {

    public ForbiddenException() {
        this("Forbidden");
    }

    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN.value(), message);
    }
}