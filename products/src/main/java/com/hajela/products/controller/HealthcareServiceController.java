package com.hajela.products.controller;

import com.hajela.products.dto.HealthcareServiceDto;
import com.hajela.products.dto.CreateHealthcareServiceDto;
import com.hajela.products.service.HealthcareServiceService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
@RequestMapping("/v1/healthcare-services")
@Tag(name = "Healthcare Services", description = "Healthcare services and treatments management")
public class HealthcareServiceController {

    private final HealthcareServiceService healthcareServiceService;

    @Operation(summary = "Get all healthcare services", description = "Retrieve paginated list of available healthcare services")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Services retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<HealthcareServiceDto>> getAllServices(
        @Parameter(description = "Pagination parameters")
        Pageable pageable,
        @Parameter(description = "Filter by category")
        @RequestParam(required = false) String category,
        @Parameter(description = "Filter by provider location")
        @RequestParam(required = false) String location) {
        
        Page<HealthcareServiceDto> services = healthcareServiceService.getAllServices(pageable, category, location);
        return ResponseEntity.ok(services);
    }

    @Operation(summary = "Get service by ID", description = "Retrieve healthcare service details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service found", 
            content = @Content(schema = @Schema(implementation = HealthcareServiceDto.class))),
        @ApiResponse(responseCode = "404", description = "Service not found")
    })
    @GetMapping("/{serviceId}")
    public ResponseEntity<HealthcareServiceDto> getServiceById(
        @Parameter(description = "Healthcare service ID", required = true)
        @PathVariable String serviceId) {
        
        return healthcareServiceService.getServiceById(serviceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Search services", description = "Search healthcare services by name or description")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed")
    })
    @GetMapping("/search")
    public ResponseEntity<List<HealthcareServiceDto>> searchServices(
        @Parameter(description = "Search query", required = true)
        @RequestParam String query,
        @Parameter(description = "Maximum results")
        @RequestParam(defaultValue = "20") int limit) {
        
        List<HealthcareServiceDto> services = healthcareServiceService.searchServices(query, limit);
        return ResponseEntity.ok(services);
    }

    @Operation(summary = "Get services by category", description = "Get healthcare services by category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Services retrieved")
    })
    @GetMapping("/category/{category}")
    public ResponseEntity<List<HealthcareServiceDto>> getServicesByCategory(
        @Parameter(description = "Service category", required = true)
        @PathVariable String category) {
        
        List<HealthcareServiceDto> services = healthcareServiceService.getServicesByCategory(category);
        return ResponseEntity.ok(services);
    }

    @Operation(summary = "Create healthcare service", description = "Create a new healthcare service (Provider only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Service created successfully", 
            content = @Content(schema = @Schema(implementation = HealthcareServiceDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid service data"),
        @ApiResponse(responseCode = "401", description = "Invalid token"),
        @ApiResponse(responseCode = "403", description = "Provider role required")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<HealthcareServiceDto> createService(
        @Parameter(description = "JWT Bearer token", required = true)
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @Parameter(description = "Healthcare service details", required = true)
        @Valid @RequestBody CreateHealthcareServiceDto serviceDto) {
        
        HealthcareServiceDto createdService = healthcareServiceService.createService(authorizationHeader, serviceDto);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{serviceId}")
                .buildAndExpand(createdService.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(createdService);
    }

    @Operation(summary = "Update healthcare service", description = "Update healthcare service (Provider only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service updated successfully"),
        @ApiResponse(responseCode = "404", description = "Service not found"),
        @ApiResponse(responseCode = "403", description = "Not authorized to update this service")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{serviceId}")
    public ResponseEntity<HealthcareServiceDto> updateService(
        @Parameter(description = "JWT Bearer token", required = true)
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @Parameter(description = "Healthcare service ID", required = true)
        @PathVariable String serviceId,
        @Parameter(description = "Updated service details", required = true)
        @Valid @RequestBody CreateHealthcareServiceDto serviceDto) {
        
        return healthcareServiceService.updateService(authorizationHeader, serviceId, serviceDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete healthcare service", description = "Delete healthcare service (Provider only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Service deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Service not found"),
        @ApiResponse(responseCode = "403", description = "Not authorized to delete this service")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{serviceId}")
    public ResponseEntity<Void> deleteService(
        @Parameter(description = "JWT Bearer token", required = true)
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @Parameter(description = "Healthcare service ID", required = true)
        @PathVariable String serviceId) {
        
        boolean deleted = healthcareServiceService.deleteService(authorizationHeader, serviceId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}