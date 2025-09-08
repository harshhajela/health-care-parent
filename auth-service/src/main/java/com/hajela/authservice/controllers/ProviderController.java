package com.hajela.authservice.controllers;

import com.hajela.authservice.config.RoleBasedSecurity;
import com.hajela.authservice.dto.AuthResponse;
import com.hajela.authservice.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/provider")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Healthcare Provider", description = "Healthcare provider specific operations and dashboard")
@SecurityRequirement(name = "bearerAuth")
public class ProviderController {

    @Operation(summary = "Get provider dashboard", description = "Get dashboard data for healthcare provider")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dashboard data retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Provider access required")
    })
    @PreAuthorize("hasRole('PROVIDER')")
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getProviderDashboard(
        @Parameter(hidden = true) @RequestAttribute("userEmail") String userEmail,
        @Parameter(hidden = true) @RequestAttribute("userId") String userId) {
        
        log.info("Provider dashboard accessed by: {} (ID: {})", userEmail, userId);
        
        Map<String, Object> dashboard = Map.of(
            "providerId", userId,
            "providerEmail", userEmail,
            "totalAppointments", 45,
            "todayAppointments", 8,
            "pendingAppointments", 3,
            "completedAppointments", 42,
            "revenue", Map.of(
                "thisMonth", 15000.0,
                "lastMonth", 12500.0,
                "growth", "+20%"
            ),
            "availability", Map.of(
                "status", "ACTIVE",
                "nextAvailableSlot", "2024-09-03T14:00:00",
                "workingHours", "09:00 - 17:00"
            ),
            "ratings", Map.of(
                "average", 4.7,
                "totalReviews", 156,
                "recentReviews", 12
            )
        );
        
        return ResponseEntity.ok(dashboard);
    }

    @Operation(summary = "Get provider appointments", description = "Get all appointments for the provider")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Appointments retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Provider access required")
    })
    @PreAuthorize("hasRole('PROVIDER')")
    @GetMapping("/appointments")
    public ResponseEntity<List<Map<String, Object>>> getProviderAppointments(
        @Parameter(hidden = true) @RequestAttribute("userId") String providerId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String status) {
        
        // Mock appointment data - replace with actual service call
        List<Map<String, Object>> appointments = List.of(
            Map.of(
                "appointmentId", "apt-001",
                "customerName", "John Doe",
                "customerEmail", "john@example.com", 
                "appointmentDate", "2024-09-03T14:00:00",
                "service", "General Consultation",
                "status", "CONFIRMED",
                "duration", 30,
                "fee", 150.0
            ),
            Map.of(
                "appointmentId", "apt-002", 
                "customerName", "Jane Smith",
                "customerEmail", "jane@example.com",
                "appointmentDate", "2024-09-03T15:00:00", 
                "service", "Follow-up Consultation",
                "status", "PENDING",
                "duration", 20,
                "fee", 100.0
            )
        );
        
        return ResponseEntity.ok(appointments);
    }

    @Operation(summary = "Update appointment status", description = "Update the status of an appointment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Appointment status updated successfully"),
        @ApiResponse(responseCode = "404", description = "Appointment not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Provider access required")
    })
    @PreAuthorize("hasRole('PROVIDER')")
    @PutMapping("/appointments/{appointmentId}/status")
    public ResponseEntity<Map<String, Object>> updateAppointmentStatus(
        @PathVariable String appointmentId,
        @RequestBody Map<String, String> statusUpdate,
        @Parameter(hidden = true) @RequestAttribute("userId") String providerId) {
        
        String newStatus = statusUpdate.get("status");
        String notes = statusUpdate.get("notes");
        
        log.info("Provider {} updating appointment {} to status: {}", providerId, appointmentId, newStatus);
        
        Map<String, Object> response = Map.of(
            "appointmentId", appointmentId,
            "status", newStatus,
            "updatedBy", providerId,
            "updatedAt", System.currentTimeMillis(),
            "notes", notes != null ? notes : ""
        );
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get provider availability", description = "Get current availability slots for the provider")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Availability retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Provider access required")
    })
    @PreAuthorize("hasRole('PROVIDER')")
    @GetMapping("/availability")
    public ResponseEntity<Map<String, Object>> getProviderAvailability(
        @Parameter(hidden = true) @RequestAttribute("userId") String providerId,
        @RequestParam(required = false) String date) {
        
        Map<String, Object> availability = Map.of(
            "providerId", providerId,
            "date", date != null ? date : "2024-09-03",
            "workingHours", Map.of(
                "start", "09:00",
                "end", "17:00",
                "breakStart", "12:00",
                "breakEnd", "13:00"
            ),
            "availableSlots", List.of(
                "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
                "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30"
            ),
            "bookedSlots", List.of("10:00", "14:00", "15:00"),
            "totalSlots", 14,
            "availableCount", 11
        );
        
        return ResponseEntity.ok(availability);
    }

    @Operation(summary = "Update provider availability", description = "Update availability slots for the provider")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Availability updated successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Provider access required")
    })
    @PreAuthorize("hasRole('PROVIDER')")
    @PutMapping("/availability")
    public ResponseEntity<Map<String, Object>> updateProviderAvailability(
        @RequestBody Map<String, Object> availabilityUpdate,
        @Parameter(hidden = true) @RequestAttribute("userId") String providerId) {
        
        log.info("Provider {} updating availability: {}", providerId, availabilityUpdate);
        
        Map<String, Object> response = Map.of(
            "providerId", providerId,
            "updatedAt", System.currentTimeMillis(),
            "status", "SUCCESS",
            "message", "Availability updated successfully"
        );
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get provider profile", description = "Get detailed provider profile information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Provider profile retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Provider access required")
    })
    @PreAuthorize("hasRole('PROVIDER')")
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProviderProfile(
        @Parameter(hidden = true) @RequestAttribute("userEmail") String userEmail,
        @Parameter(hidden = true) @RequestAttribute("userId") String userId) {
        
        Map<String, Object> profile = Map.of(
            "providerId", userId,
            "email", userEmail,
            "role", "PROVIDER",
            "personalInfo", Map.of(
                "firstName", "Dr. Sarah",
                "lastName", "Johnson",
                "phone", "+1-555-0123",
                "specialization", "Cardiologist",
                "experience", "15 years",
                "qualification", "MD, FACC"
            ),
            "clinicInfo", Map.of(
                "name", "Heart Care Clinic",
                "address", "123 Medical Center Dr, City, State 12345",
                "phone", "+1-555-0456"
            ),
            "services", List.of(
                Map.of("name", "General Consultation", "duration", 30, "fee", 150.0),
                Map.of("name", "Follow-up Visit", "duration", 20, "fee", 100.0),
                Map.of("name", "ECG Test", "duration", 15, "fee", 75.0)
            ),
            "ratings", Map.of(
                "average", 4.7,
                "totalReviews", 156
            ),
            "verified", true,
            "joinedDate", "2023-01-15"
        );
        
        return ResponseEntity.ok(profile);
    }
}