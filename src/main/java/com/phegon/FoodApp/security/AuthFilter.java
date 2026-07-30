package com.phegon.FoodApp.security;

import com.phegon.FoodApp.exceptions.CustomAuthenticationEntryPoint;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component //Create and manage this class automatically.
@RequiredArgsConstructor //This creates a constructor automatically for all final fields.So Spring can inject these objects:jwtUtils,customUserDetailsService,customAuthenticationEntryPoint
@Slf4j //This gives you a logger.It is used here:log.error(e.getMessage());
public class AuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils; //Used to:read token,get username/email from token,validate token
    private final CustomUserDetailsService customUserDetailsService; //Used to load the user from database using email.
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint; //Used when token is bad or authentication fails.


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = getTokenFromRequest(request); //This line tries to get the token from the request header.

        if (token != null){
            String email;

            try {
                email = jwtUtils.getUsernameFromToken(token);

            }catch(Exception ex){ //If reading the token fails, it means something is wrong.
                AuthenticationException authenticationException = new BadCredentialsException(ex.getMessage()); //Create an authentication exception
                customAuthenticationEntryPoint.commence(request, response, authenticationException); //Call customAuthenticationEntryPoint,That sends a clean 401 Unauthorized JSON response
                return; //This stops the request here.So invalid token means the request does not continue.
            }

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email); //If token is readable, now the filter loads the actual user from database.
            if (StringUtils.hasText(email) && jwtUtils.isTokenValid(token, userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                ); //This creates a Spring Security authentication object.This user is authenticated.
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        try {
            filterChain.doFilter(request, response);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }










    private String getTokenFromRequest(HttpServletRequest request){
        String tokenWithBearer = request.getHeader("Authorization"); //Frontend sends header like:Authorization: Bearer abcdefgh12345
        if (tokenWithBearer != null && tokenWithBearer.startsWith("Bearer ")){
            return tokenWithBearer.substring(7); //Removes "Bearer " and returns only the token part
        }
        return null;
    }
}


//A filter is something that runs before the request reaches your controller.
//protected void doFilterInternal- This is the main method of the filter.It runs for every request.
//request:The incoming request from frontend. ex:headers,URL,token
//response:The response you will send back if needed.
// filterChain:This is very important.It means:“If everything is okay, pass the request forward to the next step.”

//if (StringUtils.hasText(email) && jwtUtils.isTokenValid(token, userDetails)):This checks two things:1)StringUtils.hasText(email):Email is not null/empty/blank.
//2)jwtUtils.isTokenValid(token, userDetails):This checks:token username matches userDetails username,token is not expired
//So this means: “Only continue if token is really valid for this user.”