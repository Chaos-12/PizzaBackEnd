package com.example.demo.core.exceptions;

import java.util.HashMap;
import java.util.Map;

public class BadRequestException extends HttpException {

    private final Map<String, String> exceptionMap = new HashMap<String, String>();

    public BadRequestException() {
        this("Bad Request Exception");
    }

    public BadRequestException(String message) {
        super(400, message);
    }

    public void addException(String key, String message) {
        this.exceptionMap.put(key, message);
    }

    public Map<String, String> getExceptions() {
        return this.exceptionMap;
    }

}
