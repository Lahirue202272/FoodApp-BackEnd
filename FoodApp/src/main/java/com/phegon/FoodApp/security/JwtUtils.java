package com.phegon.FoodApp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service //Create this class as a Spring-managed service.That means Spring can create it and use it in other classes.
@Slf4j
public class JwtUtils {

    private static final long EXPIRATION_TIME = 30L * 24 * 60 * 60 *1000; //This means the token will expire after 30 days (30 days in millisecond).
    private SecretKey key;
    //This is the key used to sign and verify the token.
    //Why is this need:JWT tokens should not be easy to fake.,So when you create a token, you sign it with a secret key.,Later, when you read the token, you verify it with the same key.


    @Value("${secretJwtString}")
    private String secretJwtString; //This takes the value from your application.properties.


    @PostConstruct //This method runs automatically after Spring creates the object.
    private void init(){
        byte[] keyByte = secretJwtString.getBytes(StandardCharsets.UTF_8); //Convert the secret string into bytes.,Because the cryptographic key needs bytes, not plain text.
        this.key = new SecretKeySpec(keyByte, "HmacSHA256"); //Create a real SecretKey object using:the bytes,the algorithm HmacSHA256
    }

    public String generateToken(String email){ //This method creates a token for a user.,It takes the user email and puts it into the token.
        return Jwts.builder() //Start building the token.
                .subject(email) //This stores the user identity inside the token.
                .issuedAt(new Date(System.currentTimeMillis())) //This stores the current time when the token was created.
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) //This sets when the token will expire.
                .signWith(key) //This signs the token using your secret key.
                .compact(); //This finishes the token creation and returns the token as a string.
    }

    public String getUsernameFromToken(String token){
        return extractClaims(token, Claims::getSubject); //read the claims from token,get the subject part
    }
    //This method extracts the username from the token.,But in your case, username is actually the email.,It returns the subject stored in the token.

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){ //It reads the token, extracts all claims, and lets you choose which part you want.
        return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload()); //This does a lot:parse the token,verify signature using key,read the token payload,get claims from it
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token); //read username from token
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); //Compare it with the username from logged-in user details,Check token is not expired,If both are true, token is valid.
    }
    //This method checks whether a token is valid for a given user.
    //It checks two things:token username matches user username,token is not expired

    private boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}


//What are claims?A JWT token contains pieces of information called claims.
//Examples of claims:subject → who the token belongs to ,issuedAt → when token created ,expiration → when token expires