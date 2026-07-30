package com.phegon.FoodApp.exceptions;

public class UnauthorizedAccessException extends RuntimeException{

    public UnauthorizedAccessException(String message){//This is a constructor.
        super(message); //This sends the message to the parent class, which is RuntimeException.,So Java stores that error message inside the exception.
    }
}
