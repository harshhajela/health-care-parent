package com.hajela.authservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints - no authentication required
                .requestMatchers(
                    "/v1/auth/login",
                    "/v1/auth/register", 
                    "/v1/auth/refreshToken",
                    "/v1/auth/activate",
                    "/v1/auth/forgotPassword",
                    "/v1/auth/resetPassword",
                    // Swagger/OpenAPI endpoints
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    // Actuator endpoints
                    "/actuator/health",
                    "/actuator/info"
                ).permitAll()
                
                // Role-based access control
                .requestMatchers("/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/v1/provider/**").hasRole("PROVIDER") 
                .requestMatchers("/v1/customer/**").hasRole("CUSTOMER")
                
                // Customer endpoints - accessible by customers and admins
                .requestMatchers("/v1/auth/logout", "/v1/user/**").hasAnyRole("CUSTOMER", "PROVIDER", "ADMIN")
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}