package com.solutions.interview.exceptionHandling;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
}
