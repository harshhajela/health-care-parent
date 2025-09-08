package com.hajela.profileservice.controller;

import com.hajela.profileservice.dto.ProviderProfileDto;
import com.hajela.profileservice.dto.ProvidersDto;
import com.hajela.profileservice.entity.ProviderProfileEntity;
import com.hajela.profileservice.service.ProviderProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/provider/profile")
@RequiredArgsConstructor
@Tag(name = "Provider Profile", description = "Healthcare provider profile management operations")
@SecurityRequirement(name = "bearerAuth")
public class ProviderProfileController {

    private final ProviderProfileService providerService;

    @GetMapping
    public ResponseEntity<Page<ProvidersDto>> getAllProviders(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(providerService.getAllProviders(pageable));
    }

    @GetMapping("/{email}")
    public ResponseEntity<ProviderProfileEntity> getProviderByEmail(@PathVariable String email) {
        Optional<ProviderProfileEntity> entityOptional = providerService.getProviderByEmail(email);
        return entityOptional.map(providerProfileEntity ->
                new ResponseEntity<>(providerProfileEntity, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping
    public ResponseEntity<ProviderProfileEntity> updateProviderProfile(@RequestBody ProviderProfileDto providerDTO,
                                                                @RequestHeader(name = "Authorization") String authorizationHeader) {
        Optional<ProviderProfileEntity> updatedProvider =
                providerService.updateProvider(authorizationHeader, providerDTO);
        return updatedProvider.map(providerProfileEntity ->
                new ResponseEntity<>(providerProfileEntity, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/services")
    public ResponseEntity<ProviderProfileEntity> updateProviderServices(@RequestBody ProviderProfileDto providerDTO,
                                                                @RequestHeader(name = "Authorization") String authorizationHeader) {
        Optional<ProviderProfileEntity> updatedServices =
                providerService.updateServices(authorizationHeader, providerDTO);
        return updatedServices.map(providerProfileEntity ->
                        new ResponseEntity<>(providerProfileEntity, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
