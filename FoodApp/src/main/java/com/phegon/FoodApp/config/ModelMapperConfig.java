package com.phegon.FoodApp.config;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){ //ModelMapper → return type and modelMapper → method name
        ModelMapper modelMapper = new ModelMapper(); //This creates a new ModelMapper object.
        modelMapper.getConfiguration() // accesses the settings of the ModelMapper.
                .setFieldMatchingEnabled(true) // tells ModelMapper: “Match fields directly by name.” ,both objects have fields with the same name, ModelMapper can copy them.
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE) // tells ModelMapper:“You are allowed to access even private fields.”
                .setMatchingStrategy(MatchingStrategies.STANDARD); // This tells ModelMapper how strictly it should match names.,STANDARD means:“Use the normal smart matching rules.”

        return modelMapper;
    }
}

//@Configuration → this class is for setup
//class ModelMapperConfig → setup class name
//@Bean → Spring should store the returned object
//public ModelMapper modelMapper() → a method that returns a ModelMapper object
//new ModelMapper() → create the object
//getConfiguration() → open its settings
//setFieldMatchingEnabled(true) → allow automatic field matching
//setFieldAccessLevel(PRIVATE) → allow access to private fields
//setMatchingStrategy(STANDARD) → use normal matching rules
//return modelMapper → give the configured object to Spring
