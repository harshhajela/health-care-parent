package com.hajela.profileservice.controller;

import com.hajela.profileservice.dto.CustomerProfileDto;
import com.hajela.profileservice.entity.CustomerProfileEntity;
import com.hajela.profileservice.entity.SaveCustomerProfileDto;
import com.hajela.profileservice.exceptions.ProfileNotFoundException;
import com.hajela.profileservice.service.CustomerProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/customer/profile")
@Tag(name = "Customer Profile", description = "Customer profile management operations")
@SecurityRequirement(name = "bearerAuth")
public class CustomerProfileController {

    private final CustomerProfileService customerProfileService;

    @Operation(summary = "Get customer profile", description = "Retrieve the customer's profile information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
            content = @Content(schema = @Schema(implementation = CustomerProfileDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @GetMapping
    public ResponseEntity<CustomerProfileDto> getCustomerProfile(
            @Parameter(description = "JWT Authorization token", required = true) @RequestHeader(name = "Authorization") String authorizationHeader) {
        return customerProfileService.getCustomerProfileData(authorizationHeader).stream()
                .map(CustomerProfileDto::from)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ProfileNotFoundException("Customer profile not found"));
    }

    @Operation(summary = "Save customer profile", description = "Create or update customer profile information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Profile saved successfully",
            content = @Content(schema = @Schema(implementation = CustomerProfileDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid profile data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token")
    })
    @PostMapping
    public ResponseEntity<CustomerProfileDto> saveCustomerProfile(
            @Parameter(description = "Customer profile data", required = true) @Validated @RequestBody SaveCustomerProfileDto saveCustomerProfileDto,
            @Parameter(description = "JWT Authorization token", required = true) @RequestHeader(name = "Authorization") String authorizationHeader) {
        CustomerProfileEntity customerProfileEntity = customerProfileService.saveCustomerProfile(
                saveCustomerProfileDto, authorizationHeader);
        CustomerProfileDto customerProfileDto = CustomerProfileDto.from(customerProfileEntity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(uri).body(customerProfileDto);
    }
}
