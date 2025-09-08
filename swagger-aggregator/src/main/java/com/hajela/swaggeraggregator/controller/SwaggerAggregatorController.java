package com.hajela.swaggeraggregator.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v3")
@RequiredArgsConstructor
public class SwaggerAggregatorController {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.auth-service.url:http://localhost:8081}")
    private String authServiceUrl;

    @Value("${services.profile-service.url:http://localhost:8082}")
    private String profileServiceUrl;

    @Value("${services.booking-service.url:http://localhost:8083}")
    private String bookingServiceUrl;

    @Value("${services.products.url:http://localhost:8084}")
    private String productsServiceUrl;

    @Value("${services.notification-service.url:http://localhost:8085}")
    private String notificationServiceUrl;

    @GetMapping("/api-docs")
    public Mono<ResponseEntity<Map<String, Object>>> getUnifiedApiDocs() {
        log.info("Generating unified API documentation");
        
        Map<String, Object> unifiedSpec = createBaseSpec();
        
        return fetchAllServiceDocs()
                .map(serviceDocs -> {
                    mergeServiceDocs(unifiedSpec, serviceDocs);
                    return ResponseEntity.ok(unifiedSpec);
                })
                .onErrorReturn(ResponseEntity.status(503)
                        .body(Map.of("error", "Some services are not available")));
    }

    @GetMapping("/api-docs/{service}")
    public Mono<ResponseEntity<Object>> getServiceApiDocs(@PathVariable String service) {
        String serviceUrl = getServiceUrl(service);
        if (serviceUrl == null) {
            return Mono.just(ResponseEntity.notFound().build());
        }

        return webClientBuilder.build()
                .get()
                .uri(serviceUrl + "/v3/api-docs")
                .retrieve()
                .bodyToMono(Object.class)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(503).build());
    }

    private Map<String, Object> createBaseSpec() {
        Map<String, Object> spec = new HashMap<>();
        spec.put("openapi", "3.0.3");
        
        Map<String, Object> info = new HashMap<>();
        info.put("title", "Healthcare Platform - Unified API");
        info.put("description", "Complete API Documentation for Healthcare Microservices");
        info.put("version", "v1.0");
        spec.put("info", info);
        
        spec.put("paths", new HashMap<>());
        spec.put("components", new HashMap<>());
        
        return spec;
    }

    private Mono<Map<String, Object>> fetchAllServiceDocs() {
        Map<String, String> services = Map.of(
                "auth-service", authServiceUrl,
                "profile-service", profileServiceUrl,
                "booking-service", bookingServiceUrl,
                "products", productsServiceUrl,
                "notification-service", notificationServiceUrl
        );

        Map<String, Object> allDocs = new HashMap<>();
        
        // For simplicity, we'll just create a basic merged structure
        // In a production system, you'd want to properly merge the OpenAPI specs
        return Mono.just(allDocs);
    }

    private void mergeServiceDocs(Map<String, Object> unifiedSpec, Map<String, Object> serviceDocs) {
        // Basic merging logic - in production, you'd want more sophisticated merging
        Map<String, Object> paths = (Map<String, Object>) unifiedSpec.get("paths");
        
        // Add service information to the unified spec
        paths.put("/auth", Map.of(
                "description", "Authentication Service - Available at " + authServiceUrl + "/swagger-ui.html"
        ));
        paths.put("/profile", Map.of(
                "description", "Profile Service - Available at " + profileServiceUrl + "/swagger-ui.html"
        ));
        paths.put("/booking", Map.of(
                "description", "Booking Service - Available at " + bookingServiceUrl + "/swagger-ui.html"
        ));
        paths.put("/healthcare-services", Map.of(
                "description", "Products Service - Available at " + productsServiceUrl + "/swagger-ui.html"
        ));
        paths.put("/notifications", Map.of(
                "description", "Notification Service - Available at " + notificationServiceUrl + "/swagger-ui.html"
        ));
    }

    private String getServiceUrl(String service) {
        return switch (service) {
            case "auth-service", "01-authentication" -> authServiceUrl;
            case "profile-service", "02-profiles" -> profileServiceUrl;
            case "booking-service", "03-bookings" -> bookingServiceUrl;
            case "products", "04-healthcare-services" -> productsServiceUrl;
            case "notification-service", "05-notifications" -> notificationServiceUrl;
            default -> null;
        };
    }
}