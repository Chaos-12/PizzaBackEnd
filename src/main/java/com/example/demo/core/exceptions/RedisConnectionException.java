package com.example.demo.core.exceptions;

public class RedisConnectionException extends HttpException{
    public RedisConnectionException(String message){
        super(500,message);
    }

    public RedisConnectionException(){
        this("Under construction");
    }
}
