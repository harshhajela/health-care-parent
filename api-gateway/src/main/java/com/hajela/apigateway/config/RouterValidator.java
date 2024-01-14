package com.hajela.apigateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Slf4j
@Service
public class RouterValidator {

    public static final List<String> openEndpoints = List.of(
            "/v1/auth/register",
            "/v1/auth/login",
            "/v1/auth/logout",
            "/v1/auth/refreshToken",
            "/v1/auth/activate",
            "/v1/auth/forgotPassword",
            "/v1/auth/resetPassword"
    );

    public static final Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
