package com.example.demo.core.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpException {

    private final Map<String, String> exceptionMap = new HashMap<String, String>();

    public BadRequestException() {
        this("Bad Request");
    }

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }

    public void addException(String key, String message) {
        this.exceptionMap.put(key, message);
    }

    @Override
    public String getMessage(){
        if(this.exceptionMap.isEmpty()){
            return super.getMessage();
        }
        return exceptionMap.toString();
    }
}
