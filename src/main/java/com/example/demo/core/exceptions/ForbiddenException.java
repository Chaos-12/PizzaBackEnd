package com.example.demo.core.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends HttpException {

    private final Map<String, String> exceptionMap = new HashMap<String, String>();

    public ForbiddenException() {
        this("Forbidden");
    }

    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN.value(), message);
    }

    public void addException(String key, String message) {
        this.exceptionMap.put(key, message);
    }

    public Map<String, String> getExceptions() {
        return this.exceptionMap;
    }
}
