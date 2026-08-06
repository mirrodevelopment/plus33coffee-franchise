package com.plus33.franchise.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * =========================================================================
 * SecurityConfig — Spring Security Configuration
 * =========================================================================
 *
 * Security Rules:
 *   PUBLIC  (no auth required):
 *     POST /api/applications   → Apply form submission
 *     POST /api/inquiries      → Contact form
 *     GET  /actuator/health    → Health check probe
 *
 *   ADMIN  (Basic Auth required):
 *     GET/PATCH/DELETE /api/applications/**  → Manage applications
 *     GET /api/inquiries/**                  → View inquiries
 *
 * CORS: allows the static frontend (live-server / local dev) to call the API.
 *
 * NOTE: Replace in-memory credentials with a proper user store (DB + JWT)
 *       before deploying to production.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for stateless REST API
            .csrf(AbstractHttpConfigurer::disable)

            // Configure CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Session management — stateless REST
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public endpoints (called directly from the frontend)
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/applications").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/inquiries").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/actuator/info").permitAll()

                // All other endpoints require ADMIN role
                .anyRequest().hasRole("ADMIN")
            )

            // HTTP Basic Auth for admin API calls
            .httpBasic(basic -> {});

        return http.build();
    }

    /**
     * In-memory admin user. Replace with DB-backed users in production.
     * Credentials: admin / plus33admin (change in application.properties)
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("plus33admin"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CORS configuration — permits the static frontend to call the API.
     * Add your production domain here.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
            "http://localhost:5500",      // VS Code Live Server
            "http://127.0.0.1:5500",      // VS Code Live Server
            "http://localhost:3000",      // React / Vite dev
            "http://localhost:8080",      // Local preview
            "https://plus33cafe.com",     // Production domain (update as needed)
            "https://www.plus33cafe.com"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}
