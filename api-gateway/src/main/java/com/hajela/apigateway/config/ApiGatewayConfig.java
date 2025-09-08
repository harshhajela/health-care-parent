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
    public static final String LB_PROFILE_SERVICE = "lb://PROFILE-SERVICE";
    public static final String LB_BOOKING_SERVICE = "lb://BOOKING-SERVICE";
    public static final String LB_PRODUCTS_SERVICE = "lb://PRODUCTS";
    public static final String LB_NOTIFICATION_SERVICE = "lb://NOTIFICATION-SERVICE";

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, AuthenticationFilter filter) {
        return builder.routes()

                // AUTH-SERVICE
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

                .route("auth-service-reset-user-password", r ->
                        r.path("/v1/users/reset-password/**")
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


                // PROFILE-SERVICE
                .route("provider-profile-get", r ->
                        r.path("/v1/provider/profile/**")
                                .filters(f -> f.filter(filter))
                                .uri(LB_PROFILE_SERVICE))
                .route("provider-profile-post", r ->
                        r.path("/v1/provider/profile")
                                .filters(f -> f.filter(filter))
                                .uri(LB_PROFILE_SERVICE))

                .route("provider-documents", r ->
                        r.path("/v1/provider/documents")
                                .filters(f -> f.filter(filter))
                                .uri(LB_PROFILE_SERVICE))

                /*CUSTOMER PROFILE*/
                .route("customer-profile", r ->
                        r.path("/v1/customer/profile")
                                .filters(f -> f.filter(filter))
                                .uri(LB_PROFILE_SERVICE))

                //BOOKING SERVICE ROUTES
                .route("create-booking", r ->
                        r.path("/v1/bookings")
                                .filters(f -> f.filter(filter))
                                .uri(LB_BOOKING_SERVICE))
                .route("booking-api-calls", r ->
                        r.path("/v1/bookings/**")
                                .filters(f -> f.filter(filter))
                                .uri(LB_BOOKING_SERVICE))

                // DASHBOARD ROUTES
                .route("customer-dashboard", r ->
                        r.path("/v1/customer/dashboard")
                                .filters(f -> f.filter(filter))
                                .uri(LB_AUTH_SERVICE))

                .route("provider-dashboard", r ->
                        r.path("/v1/provider/dashboard")
                                .filters(f -> f.filter(filter))
                                .uri(LB_AUTH_SERVICE))

                .route("admin-dashboard", r ->
                        r.path("/v1/admin/dashboard")
                                .filters(f -> f.filter(filter))
                                .uri(LB_AUTH_SERVICE))

                // PRODUCTS SERVICE ROUTES
                .route("healthcare-services", r ->
                        r.path("/v1/healthcare-services/**")
                                .filters(f -> f.filter(filter))
                                .uri(LB_PRODUCTS_SERVICE))

                // NOTIFICATION SERVICE ROUTES
                .route("notifications", r ->
                        r.path("/v1/notifications/**")
                                .filters(f -> f.filter(filter))
                                .uri(LB_NOTIFICATION_SERVICE))

                // PROVIDER LOCATION ROUTES
                .route("provider-location", r ->
                        r.path("/v1/provider/locations/**")
                                .filters(f -> f.filter(filter))
                                .uri(LB_BOOKING_SERVICE))

                // WEBSOCKET ROUTES
                .route("booking-websocket", r ->
                        r.path("/ws/**")
                                .filters(f -> f.filter(filter))
                                .uri(LB_BOOKING_SERVICE))

                .build();
    }

}
