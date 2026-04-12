package com.phegon.FoodApp.exceptions;

import com.phegon.FoodApp.response.Response;
import jakarta.servlet.ServletException;//a web-related exception
import jakarta.servlet.http.HttpServletRequest;//the incoming request from the client
import jakarta.servlet.http.HttpServletResponse;//the outgoing response back to the client
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor//This automatically creates a constructor for final fields.
//This is a class named CustomAuthenticationEntryPoint.,It implements AuthenticationEntryPoint.,
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    //This means the class needs an ObjectMapper object.
    //why=Because you want to convert your Response<?> object into JSON text before sending it to the client.
    //So Lombok creates a constructor automatically and Spring can inject that object.
    // Meaning = You don’t write the constructor manually; Lombok writes it for you.

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        Response<?> errorResponse = Response.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())// 401 Error
                .message(authException.getMessage())
                .build();

        response.setContentType("application/json"); //This tells the client:“I am sending JSON data.”
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); //This sets the actual HTTP status to 401.
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));//Converts the Java errorResponse object into a JSON string.
    }
}
