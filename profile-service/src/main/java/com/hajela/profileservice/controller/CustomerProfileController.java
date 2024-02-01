package com.hajela.profileservice.controller;

import com.hajela.profileservice.dto.CustomerProfileDto;
import com.hajela.profileservice.entity.CustomerProfileEntity;
import com.hajela.profileservice.entity.SaveCustomerProfileDto;
import com.hajela.profileservice.exceptions.ProfileNotFoundException;
import com.hajela.profileservice.service.CustomerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/customer/profile")
public class CustomerProfileController {

    private final CustomerProfileService customerProfileService;

    @GetMapping
    public ResponseEntity<CustomerProfileDto> getCustomerProfile(@RequestHeader(name = "Authorization") String authorizationHeader) {
        return customerProfileService.getCustomerProfileData(authorizationHeader).stream()
                .map(CustomerProfileDto::from)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ProfileNotFoundException("Customer profile not found"));
    }

    @PostMapping
    public ResponseEntity<CustomerProfileDto> saveCustomerProfile(
            @Validated @RequestBody SaveCustomerProfileDto saveCustomerProfileDto,
            @RequestHeader(name = "Authorization") String authorizationHeader) {
        CustomerProfileEntity customerProfileEntity = customerProfileService.saveCustomerProfile(
                saveCustomerProfileDto, authorizationHeader);
        CustomerProfileDto customerProfileDto = CustomerProfileDto.from(customerProfileEntity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(uri).body(customerProfileDto);
    }
}
