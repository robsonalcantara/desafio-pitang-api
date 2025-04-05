package com.pitang.desafiopitangapi.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class for the application, setting up authentication,
 * authorization, CORS filter, and session management policies.
 *
 * This class enables Spring Security and configures the necessary security filters
 * for JWT-based authentication, stateless session policy, and permissions for specific
 * API endpoints.
 */
@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    SecurityFilter securityFilter;

    /**
     * Configures security filters and authentication policies for the application.
     * Disables CSRF protection, sets session management to stateless, and defines
     * authorization rules for various API endpoints.
     *
     * @param http the HttpSecurity object used to configure security permissions
     * @return the Spring Security configuration for HTTP requests
     * @throws Exception in case of a configuration failure
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable()) // OU, se configurado corretamente, use .cors()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    /**
     * Provides the password encoder used for hashing passwords in the system.
     *
     * @return the BCryptPasswordEncoder instance for password hashing
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the AuthenticationManager needed for authenticating users.
     *
     * @param authenticationConfiguration Spring's authentication configuration
     * @return the configured AuthenticationManager
     * @throws Exception if the configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
