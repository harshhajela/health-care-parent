package com.hajela.apigateway.config;

import com.hajela.apigateway.services.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RefreshScope
@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilter {

    private final JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (RouterValidator.isSecured.test(request)) {
            if (authMissing(request)) {
                log.info("Unauthorized call received! {}", request.getPath());
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }
            final String token = request.getHeaders()
                    .getOrEmpty("Authorization")
                    .getFirst().substring(7);

            try {
                if (jwtUtils.isExpired(token)) {
                    log.info("JWT Token has expired! {}", request.getPath());
                    return onError(exchange, HttpStatus.UNAUTHORIZED);
                }
            } catch (Exception e) {
                log.info("ERROR: JWT token has expired: {}", e.getMessage());
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }
        }
        return chain.filter(exchange);
    }

    private boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
