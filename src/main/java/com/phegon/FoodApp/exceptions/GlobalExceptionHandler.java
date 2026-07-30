package com.phegon.FoodApp.exceptions;

import com.phegon.FoodApp.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;//This is used to return a full HTTP response.
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler; // This is used to mark methods that handle specific exceptions.

@ControllerAdvice //“This class should watch all controllers and handle exceptions globally.”,That means this class is not for one controller only.
//GlobalExceptionHandler,it handles exceptions globally, meaning across the full project.
public class GlobalExceptionHandler  {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<?>> handleAllUnknownExceptions(Exception ex){

        //This creates a response object.
        Response<?> response = Response.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()) //means status code 500.
                .message(ex.getMessage()) //gets the message from the error
                .build(); //This finishes creating the response object
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR); //This sends the response back with HTTP 500 status.,client gets:a response body,a 500 status
        //ResponseEntity is a Spring class used to create a full HTTP response.
        //A full HTTP response usually contains:body,status code,sometimes headers
        //Here you are sending:body = response ,status = HttpStatus.INTERNAL_SERVER_ERROR
        //So ResponseEntity is like a package for the final reply.
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response<?>> handleNotFoundException(NotFoundException ex){

        Response<?> response = Response.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response<?>> handleBadRequestException(BadRequestException ex){

        Response<?> response = Response.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentProcessingException.class)
    public ResponseEntity<Response<?>> handlePaymentProcessingException(PaymentProcessingException ex){

        Response<?> response = Response.builder()
                .statusCode(HttpStatus.BAD_GATEWAY.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Response<?>> handleUnauthorizedAccessException(UnauthorizedAccessException ex){

        Response<?> response = Response.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
        //A user tries to access admin-only data without logging in properly.,ex:“You are not allowed.”
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<?>> handleIllegalArgumentException(IllegalArgumentException ex){

        Response<?> response = Response.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}

//public ResponseEntity<Response<?>>
//This means the method returns:a ResponseEntity,containing your custom Response,<?> means the response data type can be anything, or unspecified
//Just think: “This method returns a response object to the client.”

//handleAllUnknownExceptions(Exception ex)
//This is just the method name and parameter.
//method name = handleAllUnknownExceptions
//parameter = Exception ex
//ex is the actual error object that happened.:It contains things like the error message.
