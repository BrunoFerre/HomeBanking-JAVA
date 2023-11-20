package com.mindhub.brothers.homebanking.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class ConfigCors implements WebMvcConfigurer {
    @Value("${cors.mapping}")
    private String MAPPING;

    @Value("${cors.allowed-origins}")
    private String [] ORIGINS;

    @Value("${cors.allowed-methods}")
    private String [] METHODS;

    @Value("${cors.allowed-head}")
    private String [] HEADS;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(MAPPING)
                .allowedOrigins(ORIGINS)
                .allowedMethods(METHODS)
                .allowedHeaders(HEADS)
                .allowCredentials(false)
                .maxAge(3600);
    }
}
