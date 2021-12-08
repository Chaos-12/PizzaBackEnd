package com.example.demo.core.exceptions;

import org.springframework.http.HttpStatus;

public class RedisConnectionException extends HttpException{
    public RedisConnectionException(String message){
        super(HttpStatus.INTERNAL_SERVER_ERROR.value(),message);
    }

    public RedisConnectionException(){
        this("Under construction");
    }
}
