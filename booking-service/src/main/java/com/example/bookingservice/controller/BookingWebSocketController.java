package com.example.bookingservice.controller;

import com.example.bookingservice.service.BookingNotificationService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
@Hidden // Hide from Swagger as these are WebSocket endpoints
public class BookingWebSocketController {

    private final BookingNotificationService notificationService;

    @MessageMapping("/provider/location")
    public void updateProviderLocation(@Payload ProviderLocationDto locationDto, 
                                     SimpMessageHeaderAccessor headerAccessor,
                                     Principal principal) {
        log.info("Received location update from provider: {}", principal.getName());
        
        // Extract provider email from JWT token in principal
        String providerEmail = principal.getName();
        
        notificationService.notifyProviderLocationUpdate(
            providerEmail, 
            locationDto.getLatitude(), 
            locationDto.getLongitude()
        );
    }

    @MessageMapping("/booking/join")
    public void joinBookingRoom(@Payload JoinBookingRoomDto joinRequest,
                               SimpMessageHeaderAccessor headerAccessor,
                               Principal principal) {
        log.info("User {} joining booking room for booking: {}", 
            principal.getName(), joinRequest.getBookingId());
        
        // Add user to booking-specific subscription
        headerAccessor.getSessionAttributes().put("bookingId", joinRequest.getBookingId());
        headerAccessor.getSessionAttributes().put("userEmail", principal.getName());
    }

    public static class ProviderLocationDto {
        private Double latitude;
        private Double longitude;

        public ProviderLocationDto() {}

        public ProviderLocationDto(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
    }

    public static class JoinBookingRoomDto {
        private String bookingId;

        public JoinBookingRoomDto() {}

        public JoinBookingRoomDto(String bookingId) {
            this.bookingId = bookingId;
        }

        public String getBookingId() { return bookingId; }
        public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    }
}