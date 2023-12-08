package com.hajela.apigateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ApiGatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, AuthenticationFilter filter) {
        return builder.routes()

                .route("auth-service-register", r ->
                        r.path("/v1/auth/register")
                                .filters(f -> f.filter(filter))
                                .uri("lb://auth-service"))

                .route("auth-service-login", r ->
                        r.path("/v1/auth/login")
                                .filters(f -> f.filter(filter))
                                .uri("lb://auth-service"))

                .route("auth-service-refreshToken", r ->
                        r.path("/v1/auth/refreshToken")
                                .filters(f -> f.filter(filter))
                                .uri("lb://auth-service"))

                .route("user-service-get-all", r ->
                        r.path("/v1/users")
                                .filters(f -> f.filter(filter))
                                .uri("lb://auth-service"))

                .route("user-service-get", r ->
                        r.path("/v1/users/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://auth-service"))

                .route("provider-profile-get", r ->
                        r.path("/v1/provider/profile/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://provider-service"))
                .route("provider-profile-post", r ->
                        r.path("/v1/provider/profile")
                                .filters(f -> f.filter(filter))
                                .uri("lb://provider-service"))

                .route("provider-documents", r ->
                        r.path("/v1/provider/documents")
                                .filters(f -> f.filter(filter))
                                .uri("lb://provider-service"))

                .build();
    }

}
