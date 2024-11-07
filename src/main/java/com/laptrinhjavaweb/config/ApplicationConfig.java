package com.laptrinhjavaweb.config;

import jakarta.servlet.Filter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.laptrinhjavaweb")
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public Filter sitemeshFilter(){
        return new MySiteMeshFilter();
    }
}
