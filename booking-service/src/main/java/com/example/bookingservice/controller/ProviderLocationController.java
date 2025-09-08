package com.example.bookingservice.controller;

import com.example.bookingservice.service.BookingNotificationService;
import com.example.bookingservice.service.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/provider")
@Tag(name = "Provider Location", description = "Real-time provider location tracking operations")
public class ProviderLocationController {

    private final BookingNotificationService notificationService;
    private final JwtUtils jwtUtils;

    @Operation(summary = "Update provider location", description = "Update provider's real-time location for tracking")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Location updated successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid token"),
        @ApiResponse(responseCode = "403", description = "Provider role required")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/location")
    public ResponseEntity<Void> updateLocation(
        @Parameter(description = "JWT Bearer token", required = true)
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @Parameter(description = "Location coordinates", required = true)
        @RequestBody LocationUpdateDto locationDto) {
        
        String providerEmail = jwtUtils.getEmailFromHeader(authorizationHeader);
        String role = jwtUtils.getRoleFromHeader(authorizationHeader);
        
        if (!"PROVIDER".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).build();
        }
        
        notificationService.notifyProviderLocationUpdate(
            providerEmail, 
            locationDto.getLatitude(), 
            locationDto.getLongitude()
        );
        
        return ResponseEntity.ok().build();
    }

    public static class LocationUpdateDto {
        private Double latitude;
        private Double longitude;

        public LocationUpdateDto() {}

        public LocationUpdateDto(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
    }
}