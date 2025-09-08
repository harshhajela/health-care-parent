package com.hajela.commons.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
@Tag(name = "Health Check", description = "Service health monitoring endpoints")
public class HealthCheckController {

    @Value("${spring.application.name:unknown}")
    private String serviceName;

    @Value("${server.port:8080}")
    private String serverPort;

    private final HealthEndpoint healthEndpoint;

    @Operation(summary = "Basic health check", description = "Simple health status endpoint")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service is healthy",
            content = @Content(schema = @Schema(implementation = HealthStatus.class))),
        @ApiResponse(responseCode = "503", description = "Service is unhealthy")
    })
    @GetMapping
    public ResponseEntity<HealthStatus> health() {
        try {
            var health = healthEndpoint.health();
            boolean isHealthy = Status.UP.equals(health.getStatus());
            
            HealthStatus status = HealthStatus.builder()
                    .service(serviceName)
                    .status(isHealthy ? "UP" : "DOWN")
                    .timestamp(LocalDateTime.now())
                    .port(serverPort)
                    .build();
            
            return isHealthy ? 
                ResponseEntity.ok(status) : 
                ResponseEntity.status(503).body(status);
                
        } catch (Exception e) {
            HealthStatus status = HealthStatus.builder()
                    .service(serviceName)
                    .status("DOWN")
                    .timestamp(LocalDateTime.now())
                    .port(serverPort)
                    .error(e.getMessage())
                    .build();
            
            return ResponseEntity.status(503).body(status);
        }
    }

    @Operation(summary = "Detailed health check", description = "Comprehensive health status with dependencies")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service details retrieved",
            content = @Content(schema = @Schema(implementation = DetailedHealthStatus.class)))
    })
    @GetMapping("/detailed")
    public ResponseEntity<DetailedHealthStatus> detailedHealth() {
        try {
            var health = healthEndpoint.health();
            boolean isHealthy = Status.UP.equals(health.getStatus());
            
            Map<String, Object> details = new HashMap<>();
            try {
                if (health instanceof org.springframework.boot.actuate.health.Health) {
                    var healthObj = (org.springframework.boot.actuate.health.Health) health;
                    details.putAll(healthObj.getDetails());
                }
            } catch (Exception e) {
                details.put("error", "Unable to retrieve health details: " + e.getMessage());
            }
            
            DetailedHealthStatus status = DetailedHealthStatus.builder()
                    .service(serviceName)
                    .status(isHealthy ? "UP" : "DOWN")
                    .timestamp(LocalDateTime.now())
                    .port(serverPort)
                    .version("1.0")
                    .dependencies(details)
                    .build();
            
            return ResponseEntity.ok(status);
            
        } catch (Exception e) {
            DetailedHealthStatus status = DetailedHealthStatus.builder()
                    .service(serviceName)
                    .status("DOWN")
                    .timestamp(LocalDateTime.now())
                    .port(serverPort)
                    .version("1.0")
                    .error(e.getMessage())
                    .dependencies(new HashMap<>())
                    .build();
            
            return ResponseEntity.ok(status);
        }
    }

    @lombok.Builder
    @lombok.Data
    public static class HealthStatus {
        private String service;
        private String status;
        private LocalDateTime timestamp;
        private String port;
        private String error;
    }

    @lombok.Builder
    @lombok.Data
    public static class DetailedHealthStatus {
        private String service;
        private String status;
        private LocalDateTime timestamp;
        private String port;
        private String version;
        private String error;
        private Map<String, Object> dependencies;
    }
}