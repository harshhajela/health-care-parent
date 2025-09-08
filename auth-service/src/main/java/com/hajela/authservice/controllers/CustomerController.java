package com.hajela.authservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/customer")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customer Dashboard", description = "Customer specific operations and healthcare services")
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {

    @Operation(summary = "Get customer dashboard", description = "Get personalized dashboard for customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dashboard data retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Customer access required")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getCustomerDashboard(
        @Parameter(hidden = true) @RequestAttribute("userEmail") String userEmail,
        @Parameter(hidden = true) @RequestAttribute("userId") String userId) {
        
        log.info("Customer dashboard accessed by: {} (ID: {})", userEmail, userId);
        
        Map<String, Object> dashboard = Map.of(
            "customerId", userId,
            "customerEmail", userEmail,
            "appointmentSummary", Map.of(
                "totalAppointments", 12,
                "upcomingAppointments", 2,
                "completedAppointments", 9,
                "cancelledAppointments", 1,
                "nextAppointment", Map.of(
                    "date", "2024-09-05T10:00:00",
                    "provider", "Dr. Sarah Johnson",
                    "service", "Follow-up Consultation",
                    "location", "Heart Care Clinic"
                )
            ),
            "healthRecords", Map.of(
                "totalRecords", 15,
                "recentDiagnoses", List.of("Hypertension", "High Cholesterol"),
                "currentMedications", List.of("Lisinopril 10mg", "Atorvastatin 20mg"),
                "allergies", List.of("Penicillin", "Shellfish")
            ),
            "favoriteProviders", List.of(
                Map.of(
                    "providerId", "2001",
                    "name", "Dr. Sarah Johnson",
                    "specialization", "Cardiologist", 
                    "rating", 4.8,
                    "lastVisit", "2024-08-15"
                ),
                Map.of(
                    "providerId", "2002",
                    "name", "Dr. Mike Wilson",
                    "specialization", "General Practitioner",
                    "rating", 4.7,
                    "lastVisit", "2024-07-20"
                )
            ),
            "recentActivity", List.of(
                Map.of("action", "Appointment completed", "provider", "Dr. Sarah Johnson", "date", "2024-08-15"),
                Map.of("action", "Prescription filled", "medication", "Lisinopril 10mg", "date", "2024-08-16"),
                Map.of("action", "Lab results available", "test", "Blood Work", "date", "2024-08-18")
            )
        );
        
        return ResponseEntity.ok(dashboard);
    }

    @Operation(summary = "Get customer appointments", description = "Get all appointments for the customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Appointments retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Customer access required")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/appointments")
    public ResponseEntity<List<Map<String, Object>>> getCustomerAppointments(
        @Parameter(hidden = true) @RequestAttribute("userId") String customerId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String status) {
        
        Map<String, Object> appointment1 = new HashMap<>();
        appointment1.put("appointmentId", "apt-001");
        appointment1.put("providerName", "Dr. Sarah Johnson");
        appointment1.put("providerSpecialization", "Cardiologist");
        appointment1.put("clinicName", "Heart Care Clinic");
        appointment1.put("appointmentDate", "2024-09-05T10:00:00");
        appointment1.put("service", "Follow-up Consultation");
        appointment1.put("status", "CONFIRMED");
        appointment1.put("duration", 30);
        appointment1.put("fee", 150.0);
        appointment1.put("canCancel", true);
        appointment1.put("canReschedule", true);

        Map<String, Object> appointment2 = new HashMap<>();
        appointment2.put("appointmentId", "apt-002");
        appointment2.put("providerName", "Dr. Mike Wilson");
        appointment2.put("providerSpecialization", "General Practitioner");
        appointment2.put("clinicName", "Family Health Center");
        appointment2.put("appointmentDate", "2024-09-10T14:00:00");
        appointment2.put("service", "Annual Checkup");
        appointment2.put("status", "CONFIRMED");
        appointment2.put("duration", 45);
        appointment2.put("fee", 200.0);
        appointment2.put("canCancel", true);
        appointment2.put("canReschedule", true);

        List<Map<String, Object>> appointments = List.of(appointment1, appointment2);
        
        return ResponseEntity.ok(appointments);
    }

    @Operation(summary = "Book new appointment", description = "Book a new appointment with a healthcare provider")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Appointment booked successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid booking data"),
        @ApiResponse(responseCode = "409", description = "Time slot not available")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/appointments")
    public ResponseEntity<Map<String, Object>> bookAppointment(
        @RequestBody Map<String, Object> appointmentRequest,
        @Parameter(hidden = true) @RequestAttribute("userId") String customerId) {
        
        log.info("Customer {} booking appointment: {}", customerId, appointmentRequest);
        
        Map<String, Object> response = Map.of(
            "appointmentId", "apt-" + System.currentTimeMillis(),
            "customerId", customerId,
            "providerId", appointmentRequest.get("providerId"),
            "appointmentDate", appointmentRequest.get("appointmentDate"),
            "service", appointmentRequest.get("service"),
            "status", "CONFIRMED",
            "bookingTime", System.currentTimeMillis(),
            "confirmationNumber", "CONF-" + System.currentTimeMillis(),
            "message", "Appointment booked successfully"
        );
        
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Cancel appointment", description = "Cancel an existing appointment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Appointment cancelled successfully"),
        @ApiResponse(responseCode = "404", description = "Appointment not found"),
        @ApiResponse(responseCode = "400", description = "Cancellation not allowed")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/appointments/{appointmentId}")
    public ResponseEntity<Map<String, Object>> cancelAppointment(
        @PathVariable String appointmentId,
        @RequestBody(required = false) Map<String, String> cancellationReason,
        @Parameter(hidden = true) @RequestAttribute("userId") String customerId) {
        
        String reason = cancellationReason != null ? cancellationReason.get("reason") : "Customer request";
        
        log.info("Customer {} cancelling appointment {}: {}", customerId, appointmentId, reason);
        
        Map<String, Object> response = Map.of(
            "appointmentId", appointmentId,
            "status", "CANCELLED",
            "cancelledBy", customerId,
            "cancelledAt", System.currentTimeMillis(),
            "reason", reason,
            "refundAmount", 150.0,
            "refundMethod", "Original payment method",
            "message", "Appointment cancelled successfully"
        );
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get available providers", description = "Search and filter available healthcare providers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Providers retrieved successfully")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/providers")
    public ResponseEntity<List<Map<String, Object>>> getAvailableProviders(
        @RequestParam(required = false) String specialization,
        @RequestParam(required = false) String location,
        @RequestParam(required = false) String date,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        
        Map<String, Object> provider1 = new HashMap<>();
        provider1.put("providerId", "2001");
        provider1.put("name", "Dr. Sarah Johnson");
        provider1.put("specialization", "Cardiologist");
        provider1.put("qualification", "MD, FACC");
        provider1.put("experience", "15 years");
        provider1.put("rating", 4.8);
        provider1.put("totalReviews", 156);
        provider1.put("clinicName", "Heart Care Clinic");
        provider1.put("address", "123 Medical Center Dr, City, State");
        provider1.put("distance", "2.3 miles");
        provider1.put("availableSlots", List.of("09:00", "10:30", "14:00", "15:30"));
        provider1.put("consultationFee", 150.0);
        provider1.put("verified", true);

        Map<String, Object> provider2 = new HashMap<>();
        provider2.put("providerId", "2002");
        provider2.put("name", "Dr. Mike Wilson");
        provider2.put("specialization", "General Practitioner");
        provider2.put("qualification", "MD, MRCGP");
        provider2.put("experience", "12 years");
        provider2.put("rating", 4.7);
        provider2.put("totalReviews", 98);
        provider2.put("clinicName", "Family Health Center");
        provider2.put("address", "456 Health Ave, City, State");
        provider2.put("distance", "1.8 miles");
        provider2.put("availableSlots", List.of("08:30", "11:00", "13:30", "16:00"));
        provider2.put("consultationFee", 120.0);
        provider2.put("verified", true);

        List<Map<String, Object>> providers = List.of(provider1, provider2);
        
        return ResponseEntity.ok(providers);
    }

    @Operation(summary = "Get customer health records", description = "Get customer's health records and medical history")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Health records retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Customer access required")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/health-records")
    public ResponseEntity<Map<String, Object>> getHealthRecords(
        @Parameter(hidden = true) @RequestAttribute("userId") String customerId) {
        
        Map<String, Object> healthRecords = Map.of(
            "customerId", customerId,
            "personalInfo", Map.of(
                "bloodType", "O+",
                "height", "5'8\"",
                "weight", "165 lbs",
                "dateOfBirth", "1990-05-15",
                "emergencyContact", Map.of(
                    "name", "Jane Doe",
                    "relationship", "Spouse",
                    "phone", "+1-555-0789"
                )
            ),
            "medicalHistory", List.of(
                Map.of(
                    "date", "2024-08-15",
                    "provider", "Dr. Sarah Johnson",
                    "diagnosis", "Hypertension",
                    "treatment", "Prescribed Lisinopril 10mg",
                    "notes", "Blood pressure elevated, lifestyle changes recommended"
                ),
                Map.of(
                    "date", "2024-07-20",
                    "provider", "Dr. Mike Wilson", 
                    "diagnosis", "High Cholesterol",
                    "treatment", "Prescribed Atorvastatin 20mg",
                    "notes", "Annual checkup, cholesterol levels elevated"
                )
            ),
            "currentMedications", List.of(
                Map.of(
                    "name", "Lisinopril", 
                    "dosage", "10mg",
                    "frequency", "Once daily",
                    "startDate", "2024-08-15",
                    "prescribedBy", "Dr. Sarah Johnson"
                ),
                Map.of(
                    "name", "Atorvastatin",
                    "dosage", "20mg", 
                    "frequency", "Once daily at bedtime",
                    "startDate", "2024-07-20",
                    "prescribedBy", "Dr. Mike Wilson"
                )
            ),
            "allergies", List.of(
                Map.of("allergen", "Penicillin", "reaction", "Rash", "severity", "Moderate"),
                Map.of("allergen", "Shellfish", "reaction", "Swelling", "severity", "Severe")
            ),
            "labResults", List.of(
                Map.of(
                    "date", "2024-08-18",
                    "test", "Complete Blood Count",
                    "results", "Normal",
                    "orderedBy", "Dr. Sarah Johnson"
                ),
                Map.of(
                    "date", "2024-07-18", 
                    "test", "Lipid Panel",
                    "results", "Elevated cholesterol",
                    "orderedBy", "Dr. Mike Wilson"
                )
            )
        );
        
        return ResponseEntity.ok(healthRecords);
    }

    @Operation(summary = "Rate and review provider", description = "Submit rating and review for a healthcare provider")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Review submitted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid review data"),
        @ApiResponse(responseCode = "409", description = "Review already exists for this appointment")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/reviews")
    public ResponseEntity<Map<String, Object>> submitReview(
        @RequestBody Map<String, Object> reviewRequest,
        @Parameter(hidden = true) @RequestAttribute("userId") String customerId) {
        
        log.info("Customer {} submitting review: {}", customerId, reviewRequest);
        
        Map<String, Object> response = Map.of(
            "reviewId", "rev-" + System.currentTimeMillis(),
            "customerId", customerId,
            "providerId", reviewRequest.get("providerId"),
            "appointmentId", reviewRequest.get("appointmentId"),
            "rating", reviewRequest.get("rating"),
            "comment", reviewRequest.get("comment"),
            "submittedAt", System.currentTimeMillis(),
            "status", "PUBLISHED",
            "message", "Review submitted successfully"
        );
        
        return ResponseEntity.status(201).body(response);
    }
}