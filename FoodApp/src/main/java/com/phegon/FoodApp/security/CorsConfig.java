package com.phegon.FoodApp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean //Create the object returned by this method and manage it.So the object returned by webMvcConfigurer() will become a Spring bean.
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") //Apply these CORS rules to all URLs in the application.
                        .allowedMethods("GET","POST","PUT","DELETE") //This means only these HTTP methods are allowed for cross-origin requests:
                        .allowedOrigins("*"); //Allow requests from any origin.* means all origins.ex:http://localhost:5173,http://127.0.0.1:3000,https://myfrontend.com
            }
        };
    }
}

//CORS is that permission:Backend gives permission to frontend to access my APIs.
