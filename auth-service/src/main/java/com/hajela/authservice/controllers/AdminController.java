package com.hajela.authservice.controllers;

import com.hajela.authservice.config.RoleBasedSecurity;
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
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin Management", description = "Administrative operations for platform management")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    @Operation(summary = "Get admin dashboard", description = "Get comprehensive admin dashboard with platform analytics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dashboard data retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getAdminDashboard(
        @Parameter(hidden = true) @RequestAttribute("userEmail") String userEmail) {
        
        log.info("Admin dashboard accessed by: {}", userEmail);
        
        Map<String, Object> dashboard = Map.of(
            "platformStats", Map.of(
                "totalUsers", 2547,
                "totalProviders", 156,
                "totalCustomers", 2391,
                "activeUsers", 1834,
                "newUsersThisMonth", 87
            ),
            "appointmentStats", Map.of(
                "totalAppointments", 15670,
                "todayAppointments", 234,
                "pendingAppointments", 45,
                "completedAppointments", 15625,
                "cancelledAppointments", 412
            ),
            "revenueStats", Map.of(
                "totalRevenue", 1567000.0,
                "thisMonthRevenue", 125000.0,
                "lastMonthRevenue", 118500.0,
                "growthRate", "+5.5%",
                "averageAppointmentValue", 125.75
            ),
            "systemHealth", Map.of(
                "status", "HEALTHY",
                "uptime", "99.9%",
                "activeServices", 8,
                "totalServices", 8,
                "lastMaintenanceDate", "2024-09-01T02:00:00"
            ),
            "recentActivity", List.of(
                Map.of("action", "New provider registered", "user", "Dr. Mike Wilson", "timestamp", System.currentTimeMillis() - 3600000),
                Map.of("action", "Appointment completed", "user", "Jane Doe", "timestamp", System.currentTimeMillis() - 1800000),
                Map.of("action", "Payment processed", "amount", "$150.00", "timestamp", System.currentTimeMillis() - 900000)
            )
        );
        
        return ResponseEntity.ok(dashboard);
    }

    @Operation(summary = "Get all users", description = "Get paginated list of all platform users with filtering")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false) String role,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String search) {
        
        // Mock user data - replace with actual service call
        List<Map<String, Object>> users = List.of(
            Map.of(
                "userId", "1001",
                "email", "john.customer@example.com",
                "role", "CUSTOMER",
                "status", "ACTIVE",
                "joinDate", "2024-01-15",
                "lastLogin", "2024-09-02T14:30:00",
                "totalAppointments", 5
            ),
            Map.of(
                "userId", "2001", 
                "email", "dr.sarah@heartclinic.com",
                "role", "PROVIDER",
                "status", "ACTIVE",
                "joinDate", "2023-11-20",
                "lastLogin", "2024-09-03T08:15:00",
                "totalAppointments", 245,
                "specialization", "Cardiologist",
                "verified", true
            ),
            Map.of(
                "userId", "3001",
                "email", "admin@healthcare.com",
                "role", "ADMIN", 
                "status", "ACTIVE",
                "joinDate", "2023-01-01",
                "lastLogin", "2024-09-03T09:00:00"
            )
        );
        
        Map<String, Object> response = Map.of(
            "users", users,
            "totalElements", 2547,
            "totalPages", 128,
            "currentPage", page,
            "pageSize", size,
            "filters", Map.of(
                "role", role != null ? role : "ALL",
                "status", status != null ? status : "ALL",
                "search", search != null ? search : ""
            )
        );
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update user status", description = "Update user account status (ACTIVE, BLOCKED, SUSPENDED)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User status updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{userId}/status")
    public ResponseEntity<Map<String, Object>> updateUserStatus(
        @PathVariable String userId,
        @RequestBody Map<String, String> statusUpdate,
        @Parameter(hidden = true) @RequestAttribute("userEmail") String adminEmail) {
        
        String newStatus = statusUpdate.get("status");
        String reason = statusUpdate.get("reason");
        
        log.info("Admin {} updating user {} status to: {} (reason: {})", adminEmail, userId, newStatus, reason);
        
        Map<String, Object> response = Map.of(
            "userId", userId,
            "previousStatus", "ACTIVE",
            "newStatus", newStatus,
            "updatedBy", adminEmail,
            "updatedAt", System.currentTimeMillis(),
            "reason", reason != null ? reason : "",
            "success", true
        );
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get platform analytics", description = "Get detailed platform analytics and reports")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Analytics retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getPlatformAnalytics(
        @RequestParam(defaultValue = "30") int days,
        @RequestParam(required = false) String reportType) {
        
        Map<String, Object> analytics = Map.of(
            "reportPeriod", days + " days",
            "reportType", reportType != null ? reportType : "SUMMARY",
            "userGrowth", Map.of(
                "newUsers", List.of(12, 18, 25, 31, 28, 35, 42),
                "totalUsers", List.of(2460, 2478, 2503, 2534, 2562, 2597, 2639),
                "labels", List.of("Week 1", "Week 2", "Week 3", "Week 4")
            ),
            "appointmentTrends", Map.of(
                "completedAppointments", List.of(245, 267, 289, 312, 298, 334, 356),
                "cancelledAppointments", List.of(15, 12, 18, 22, 16, 19, 14),
                "labels", List.of("Week 1", "Week 2", "Week 3", "Week 4")
            ),
            "revenueAnalytics", Map.of(
                "weeklyRevenue", List.of(28500, 31200, 35800, 39100, 37600, 42300, 44800),
                "averageAppointmentValue", 125.75,
                "topServices", List.of(
                    Map.of("service", "General Consultation", "count", 1245, "revenue", 186750.0),
                    Map.of("service", "Follow-up Visit", "count", 890, "revenue", 89000.0),
                    Map.of("service", "Specialist Consultation", "count", 567, "revenue", 113400.0)
                )
            ),
            "providerPerformance", Map.of(
                "topProviders", List.of(
                    Map.of("name", "Dr. Sarah Johnson", "appointments", 89, "rating", 4.8, "revenue", 13350.0),
                    Map.of("name", "Dr. Mike Wilson", "appointments", 76, "rating", 4.7, "revenue", 11400.0),
                    Map.of("name", "Dr. Lisa Chen", "appointments", 68, "rating", 4.9, "revenue", 10200.0)
                )
            )
        );
        
        return ResponseEntity.ok(analytics);
    }

    @Operation(summary = "Verify provider", description = "Verify a healthcare provider account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Provider verified successfully"),
        @ApiResponse(responseCode = "404", description = "Provider not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/providers/{providerId}/verify")
    public ResponseEntity<Map<String, Object>> verifyProvider(
        @PathVariable String providerId,
        @RequestBody Map<String, String> verificationData,
        @Parameter(hidden = true) @RequestAttribute("userEmail") String adminEmail) {
        
        String status = verificationData.get("verified"); // "true" or "false"
        String notes = verificationData.get("notes");
        
        log.info("Admin {} {} provider {}: {}", adminEmail, 
                "true".equals(status) ? "verifying" : "rejecting", providerId, notes);
        
        Map<String, Object> response = Map.of(
            "providerId", providerId,
            "verified", "true".equals(status),
            "verifiedBy", adminEmail,
            "verifiedAt", System.currentTimeMillis(),
            "notes", notes != null ? notes : "",
            "success", true
        );
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get system settings", description = "Get platform configuration and settings")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Settings retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/settings")
    public ResponseEntity<Map<String, Object>> getSystemSettings() {
        
        Map<String, Object> settings = Map.of(
            "platform", Map.of(
                "name", "Healthcare Platform",
                "version", "v2.1.0",
                "maintenanceMode", false,
                "registrationEnabled", true,
                "autoVerifyProviders", false
            ),
            "appointment", Map.of(
                "defaultDuration", 30,
                "maxBookingAdvanceDays", 60,
                "cancellationPolicy", "24_HOURS",
                "rescheduleLimit", 3
            ),
            "payment", Map.of(
                "platformFeePercentage", 5.0,
                "minimumAmount", 10.0,
                "refundPolicy", "WITHIN_7_DAYS",
                "supportedMethods", List.of("CREDIT_CARD", "DEBIT_CARD", "DIGITAL_WALLET")
            ),
            "notifications", Map.of(
                "emailNotifications", true,
                "smsNotifications", true,
                "pushNotifications", true,
                "reminderHours", List.of(24, 2)
            )
        );
        
        return ResponseEntity.ok(settings);
    }

    @Operation(summary = "Update system settings", description = "Update platform configuration and settings")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Settings updated successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/settings")
    public ResponseEntity<Map<String, Object>> updateSystemSettings(
        @RequestBody Map<String, Object> settingsUpdate,
        @Parameter(hidden = true) @RequestAttribute("userEmail") String adminEmail) {
        
        log.info("Admin {} updating system settings: {}", adminEmail, settingsUpdate.keySet());
        
        Map<String, Object> response = Map.of(
            "updatedBy", adminEmail,
            "updatedAt", System.currentTimeMillis(),
            "updatedFields", settingsUpdate.keySet(),
            "success", true,
            "message", "System settings updated successfully"
        );
        
        return ResponseEntity.ok(response);
    }
}