package com.phegon.FoodApp.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message){//This is a constructor.
        super(message); //This sends the message to the parent class, which is RuntimeException.,So Java stores that error message inside the exception.
    }
}

//This is a custom exception class.
// It is used when your program wants to say:“I looked for something, but I could not find it.”
//A constructor has:the same name as the class,no return type
//Here:class name = NotFoundException,constructor name = NotFoundException

//What does String message mean?This means when creating this exception, you can pass a message.

//package com.phegon.FoodApp.exceptions;This class belongs to the exception folder.

//public class NotFoundException:This creates a class named NotFoundException.

//extends RuntimeException:This class is a special kind of runtime exception.

//public NotFoundException(String message):This is a constructor, not a method.

// super(message);This passes the message to the parent exception class.