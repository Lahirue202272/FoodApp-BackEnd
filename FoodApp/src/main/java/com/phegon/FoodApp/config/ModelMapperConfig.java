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
