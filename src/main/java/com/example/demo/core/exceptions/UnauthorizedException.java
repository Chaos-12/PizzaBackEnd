package com.example.demo.core.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends HttpException {

    public UnauthorizedException() {
        this("Unauthorized");
    }

    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED.value(), message);
    }
}