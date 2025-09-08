package com.example.bookingservice.service;

import com.example.bookingservice.dto.BookingDto;
import com.example.bookingservice.dto.BookingStatusUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyBookingCreated(BookingDto booking) {
        log.info("Sending booking created notification for booking: {}", booking.getBookingId());
        
        // Notify customer
        messagingTemplate.convertAndSendToUser(
            booking.getCustomerEmail(),
            "/queue/booking-updates",
            new BookingStatusUpdateDto(
                booking.getBookingId(),
                booking.getBookingStatus().name(),
                "Your appointment has been booked successfully",
booking.getBookingDate()
            )
        );

        // Notify provider
        messagingTemplate.convertAndSendToUser(
            booking.getProviderEmail(),
            "/queue/booking-updates",
            new BookingStatusUpdateDto(
                booking.getBookingId(),
                booking.getBookingStatus().name(),
                "New appointment booking received",
booking.getBookingDate()
            )
        );

        // Broadcast to admin dashboard
        messagingTemplate.convertAndSend(
            "/topic/admin/bookings",
            booking
        );
    }

    public void notifyBookingStatusUpdate(BookingDto booking, String previousStatus) {
        log.info("Sending booking status update notification for booking: {} from {} to {}", 
            booking.getBookingId(), previousStatus, booking.getBookingStatus());

        String customerMessage = getCustomerStatusMessage(booking.getBookingStatus().name());
        String providerMessage = getProviderStatusMessage(booking.getBookingStatus().name());

        // Notify customer
        messagingTemplate.convertAndSendToUser(
            booking.getCustomerEmail(),
            "/queue/booking-updates",
            new BookingStatusUpdateDto(
                booking.getBookingId(),
                booking.getBookingStatus().name(),
                customerMessage,
booking.getBookingDate()
            )
        );

        // Notify provider
        messagingTemplate.convertAndSendToUser(
            booking.getProviderEmail(),
            "/queue/booking-updates",
            new BookingStatusUpdateDto(
                booking.getBookingId(),
                booking.getBookingStatus().name(),
                providerMessage,
booking.getBookingDate()
            )
        );

        // Broadcast to admin dashboard
        messagingTemplate.convertAndSend(
            "/topic/admin/booking-status-updates",
            new BookingStatusUpdateDto(
                booking.getBookingId(),
                booking.getBookingStatus().name(),
                "Booking status updated",
booking.getBookingDate()
            )
        );
    }

    public void notifyProviderLocationUpdate(String providerEmail, Double latitude, Double longitude) {
        log.info("Sending provider location update for: {}", providerEmail);
        
        messagingTemplate.convertAndSend(
            "/topic/provider-locations/" + providerEmail,
            new ProviderLocationUpdate(providerEmail, latitude, longitude, System.currentTimeMillis())
        );
    }

    private String getCustomerStatusMessage(String status) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> "Your appointment has been confirmed by the provider";
            case "CANCELLED" -> "Your appointment has been cancelled";
            case "COMPLETED" -> "Your appointment has been completed";
            case "IN_PROGRESS" -> "Your appointment is now in progress";
            case "RESCHEDULED" -> "Your appointment has been rescheduled";
            default -> "Your appointment status has been updated to " + status;
        };
    }

    private String getProviderStatusMessage(String status) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> "You have confirmed the appointment";
            case "CANCELLED" -> "The appointment has been cancelled";
            case "COMPLETED" -> "You have marked the appointment as completed";
            case "IN_PROGRESS" -> "The appointment is now in progress";
            case "RESCHEDULED" -> "The appointment has been rescheduled";
            default -> "Appointment status updated to " + status;
        };
    }

    public static class ProviderLocationUpdate {
        private String providerEmail;
        private Double latitude;
        private Double longitude;
        private Long timestamp;

        public ProviderLocationUpdate(String providerEmail, Double latitude, Double longitude, Long timestamp) {
            this.providerEmail = providerEmail;
            this.latitude = latitude;
            this.longitude = longitude;
            this.timestamp = timestamp;
        }

        // Getters and setters
        public String getProviderEmail() { return providerEmail; }
        public void setProviderEmail(String providerEmail) { this.providerEmail = providerEmail; }
        
        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
        
        public Long getTimestamp() { return timestamp; }
        public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
    }
}