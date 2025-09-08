package com.hajela.products.service;

import com.hajela.products.dto.CreateHealthcareServiceDto;
import com.hajela.products.dto.HealthcareServiceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface HealthcareServiceService {
    
    Page<HealthcareServiceDto> getAllServices(Pageable pageable, String category, String location);
    
    Optional<HealthcareServiceDto> getServiceById(String serviceId);
    
    List<HealthcareServiceDto> searchServices(String query, int limit);
    
    List<HealthcareServiceDto> getServicesByCategory(String category);
    
    HealthcareServiceDto createService(String authorizationHeader, CreateHealthcareServiceDto serviceDto);
    
    Optional<HealthcareServiceDto> updateService(String authorizationHeader, String serviceId, CreateHealthcareServiceDto serviceDto);
    
    boolean deleteService(String authorizationHeader, String serviceId);
    
    List<HealthcareServiceDto> getServicesByProvider(String providerEmail);
}