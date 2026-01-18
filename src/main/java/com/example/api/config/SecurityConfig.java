package com.example.api.config;

import java.util.List;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Provides password encoding using BCrypt (recommended for production)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures HTTP security rules and session policy
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
            .csrf(csrf -> csrf.disable()) // Disable CSRF (safe for REST APIs)
            .authorizeHttpRequests(authz -> authz
                // ✅ Public endpoints (no authentication required)
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/chat/**").permitAll()
                .requestMatchers("/api/face/**").permitAll()
                .requestMatchers("/api/whatsapp/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/api/offers/**").permitAll()

                // ✅ Allow Swagger / OpenAPI endpoints
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()

                // ✅ Bus endpoints (public for booking/search)
                .requestMatchers(HttpMethod.GET, "/buses/**").permitAll()
                .requestMatchers("/buses/confirm").permitAll()
                .requestMatchers(HttpMethod.POST, "/buses/bookSeat").permitAll()

                // ✅ Anything else requires authentication
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // REST API = stateless
            );

        return http.build();
    }

    /**
     * Defines global CORS configuration for Angular + Swagger + Postman
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ✅ Allowed origins (Angular frontend, Swagger UI, etc.)
        configuration.setAllowedOrigins(List.of(
            "http://localhost:4200",  // Angular dev server
            "http://localhost:8080"   // Swagger UI / Postman
        ));

        // ✅ Allowed HTTP methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // ✅ Allow all headers
        configuration.setAllowedHeaders(List.of("*"));

        // ✅ Allow cookies and Authorization headers (if needed)
        configuration.setAllowCredentials(true);

        // ✅ Cache duration for preflight requests
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Provides AuthenticationManager for authentication endpoints
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}