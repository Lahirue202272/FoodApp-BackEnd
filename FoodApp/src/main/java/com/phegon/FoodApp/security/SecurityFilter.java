package com.phegon.FoodApp.security;

import com.phegon.FoodApp.exceptions.CustomAccessDenialHandler;
import com.phegon.FoodApp.exceptions.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //This class contains setup/configuration code.,This class is used to configure the app.
@EnableWebSecurity //This turns on Spring Security for web requests.This says:Use Spring Security in this project.Without this, your security rules would not be active properly.
@EnableMethodSecurity //This allows security checks on methods too.Not only URLs, but methods can also be protected.
@RequiredArgsConstructor
public class SecurityFilter {

    private final AuthFilter authFilter; //This is your JWT filter.It checks every request for token.This is the main token checker.
    private final CustomAccessDenialHandler customAccessDenialHandler; //This handles 403 Forbidden.Used when:user is logged in,but not allowed
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint; //This handles 401 Unauthorized.Used when:user not logged in,token invalid,token missing


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .exceptionHandling(ex ->
                        ex.accessDeniedHandler(customAccessDenialHandler).authenticationEntryPoint(customAuthenticationEntryPoint))
                .authorizeHttpRequests(req->
                        req.requestMatchers("/api/auth/**", "/api/categories/**", "/api/menu/**", "/api/reviews/**").permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(mag->mag.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }//Passwords should not be stored as plain text.BCrypt converts the password into a hashed version.

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();
    }//This creates the AuthenticationManager.It is a Spring Security component that handles the actual authentication process.It helps check:username/email,password
}

//What is SecurityFilterChain?It is the full list of security rules that Spring will use for incoming requests.
//What is CSRF?CSRF is a security protection mainly important for session-based web apps.But your app is using JWT and stateless authentication.So in JWT APIs, CSRF is usually disabled.
//What is CORS?CORS allows frontend and backend from different origins to communicate.Here enables CORS with default settings.ex:frontend on localhost:5173,backend on localhost:8080
//accessDeniedHandler(customAccessDenialHandler):Use this when:user is authenticated,but has no permission
//authenticationEntryPoint(customAuthenticationEntryPoint):Use this when:user is not authenticated,token invalid,token missing
//requestMatchers(...).permitAll():These paths are open to everyone:api/auth/**,api/categories/**,api/menu/**,api/reviews/**
//.anyRequest().authenticated():This means:every other request must be authenticated
//.sessionManagement(mag -> mag.sessionCreationPolicy(SessionCreationPolicy.STATELESS))->This says your app is stateless.The server does not store login session in memory.Instead, each request must bring its own token.That is how JWT works.
