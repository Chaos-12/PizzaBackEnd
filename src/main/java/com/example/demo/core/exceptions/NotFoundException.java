package com.example.demo.core.exceptions;

public class NotFoundException extends HttpException {
    
    public NotFoundException(String message){
        super(404,message);
    }

    public NotFoundException(){
        this("Not found exception");
    }
}
