package com.hajela.apigateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class ApiGatewayConfig {

    public static final String LB_AUTH_SERVICE = "lb://AUTH-SERVICE";
    public static final String LB_PROVIDER_SERVICE = "lb://PROFILE-SERVICE";
    public static final String LB_BOOKING_SERVICE = "lb://BOOKING-SERVICE";

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, AuthenticationFilter filter) {
        return builder.routes()

                .route("auth-service-register", r ->
                        r.path("/v1/auth/register")
                                .filters(f -> f.filter(filter))
                                .uri(LB_AUTH_SERVICE))

                .route("auth-service-login", r ->
                        r.path("/v1/auth/login")
                                .filters(f -> f.filter(filter))
                                .uri(LB_AUTH_SERVICE))

                .route("auth-service-logout", r ->
                        r.path("/v1/auth/logout")
                                .filters(f -> f.filter(filter))
                                .uri(LB_AUTH_SERVICE))

                .route("auth-service-refreshToken", r ->
                        r.path("/v1/auth/refreshToken")
                                .filters(f -> f.filter(filter))
                                .uri(LB_AUTH_SERVICE))

                .route("auth-service-activate", r ->
                        r.path("/v1/auth/activate")
                                .filters(f -> f.filter(filter))
                                .uri(LB_AUTH_SERVICE))

                .route("auth-service-forgot-password", r ->
                        r.path("/v1/auth/forgotPassword")
                                .filters(f -> f.filter(filter))
                                .uri(LB_AUTH_SERVICE))

                .route("auth-service-reset-password", r ->
                        r.path("/v1/auth/resetPassword")
                                .filters(f -> f.filter(filter))
                                .uri(LB_AUTH_SERVICE))

                .route("user-service-get-all", r ->
                        r.path("/v1/users")
                                .filters(f -> f.filter(filter))
                                .uri(LB_AUTH_SERVICE))

                .route("user-service-get", r ->
                        r.path("/v1/users/**")
                                .filters(f -> f.filter(filter))
                                .uri(LB_AUTH_SERVICE))

                .route("provider-profile-get", r ->
                        r.path("/v1/provider/profile/**")
                                .filters(f -> f.filter(filter))
                                .uri(LB_PROVIDER_SERVICE))
                .route("provider-profile-post", r ->
                        r.path("/v1/provider/profile")
                                .filters(f -> f.filter(filter))
                                .uri(LB_PROVIDER_SERVICE))

                .route("provider-documents", r ->
                        r.path("/v1/provider/documents")
                                .filters(f -> f.filter(filter))
                                .uri(LB_PROVIDER_SERVICE))

                /*BOOKING SERVICE ROUTES*/
                .route("create-booking", r ->
                        r.path("/v1/bookings")
                                .filters(f -> f.filter(filter))
                                .uri(LB_BOOKING_SERVICE))
                .route("booking-api-calls", r ->
                        r.path("/v1/bookings/**")
                                .filters(f -> f.filter(filter))
                                .uri(LB_BOOKING_SERVICE))

                .build();
    }

}
