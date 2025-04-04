package com.pitang.desafiopitangapi.infra.cors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for handling Cross-Origin Resource Sharing (CORS) settings.
 * This class configures CORS to allow the front-end application running on localhost:4200
 * to interact with the backend.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${front.url}")
    private String frontUrl;

    /**
     * Adds CORS mappings for all endpoints, allowing specific origins, methods, and headers.
     * This method configures CORS at the controller level using Spring's WebMvcConfigurer.
     *
     * @author Robson Rodrigues
     * @param registry the registry that stores CORS configurations for different paths
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontUrl)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Authorization", "Content-Type", "Accept")
                .allowCredentials(true);
    }

    /**
     * Configures CORS using a {@link CorsConfigurationSource} bean.
     * This configuration is applied globally for all endpoints and allows
     * requests from the front-end application on localhost:4200.
     *
     * @author Robson Rodrigues
     * @return the CORS configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(frontUrl);
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
