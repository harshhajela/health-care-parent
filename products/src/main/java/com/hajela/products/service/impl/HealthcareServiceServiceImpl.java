package com.hajela.products.service.impl;

import com.hajela.products.dto.CreateHealthcareServiceDto;
import com.hajela.products.dto.HealthcareServiceDto;
import com.hajela.products.entity.HealthcareServiceEntity;
import com.hajela.products.repository.HealthcareServiceRepository;
import com.hajela.products.service.HealthcareServiceService;
import com.hajela.products.service.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthcareServiceServiceImpl implements HealthcareServiceService {

    private final HealthcareServiceRepository repository;
    private final JwtUtils jwtUtils;

    @Override
    public Page<HealthcareServiceDto> getAllServices(Pageable pageable, String category, String location) {
        Page<HealthcareServiceEntity> services;
        
        if (category != null && location != null) {
            services = repository.findByCategoryAndLocationAndIsActive(category, location, true, pageable);
        } else if (category != null) {
            services = repository.findByCategoryAndIsActive(category, true, pageable);
        } else if (location != null) {
            services = repository.findByLocationAndIsActive(location, true, pageable);
        } else {
            services = repository.findByIsActive(true, pageable);
        }
        
        return services.map(HealthcareServiceDto::from);
    }

    @Override
    public Optional<HealthcareServiceDto> getServiceById(String serviceId) {
        return repository.findByIdAndIsActive(serviceId, true)
                .map(HealthcareServiceDto::from);
    }

    @Override
    public List<HealthcareServiceDto> searchServices(String query, int limit) {
        // Simple text search - in production, consider using MongoDB text search
        List<HealthcareServiceEntity> services = repository.findByServiceNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndIsActive(
                query, query, true, org.springframework.data.domain.PageRequest.of(0, limit))
                .getContent();
        
        return services.stream()
                .map(HealthcareServiceDto::from)
                .toList();
    }

    @Override
    public List<HealthcareServiceDto> getServicesByCategory(String category) {
        return repository.findByCategoryAndIsActive(category, true)
                .stream()
                .map(HealthcareServiceDto::from)
                .toList();
    }

    @Override
    public HealthcareServiceDto createService(String authorizationHeader, CreateHealthcareServiceDto serviceDto) {
        String providerEmail = jwtUtils.getEmailFromHeader(authorizationHeader);
        String role = jwtUtils.getRoleFromHeader(authorizationHeader);
        
        if (!"PROVIDER".equalsIgnoreCase(role)) {
            throw new SecurityException("Only providers can create services");
        }
        
        // Get provider name - in production, fetch from user service
        String providerName = extractProviderName(providerEmail);
        
        HealthcareServiceEntity entity = HealthcareServiceEntity.builder()
                .serviceName(serviceDto.getServiceName())
                .description(serviceDto.getDescription())
                .category(serviceDto.getCategory())
                .price(serviceDto.getPrice())
                .durationMinutes(serviceDto.getDurationMinutes())
                .providerEmail(providerEmail)
                .providerName(providerName)
                .location(serviceDto.getLocation())
                .tags(serviceDto.getTags())
                .isActive(serviceDto.getIsActive())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        HealthcareServiceEntity savedEntity = repository.save(entity);
        return HealthcareServiceDto.from(savedEntity);
    }

    @Override
    public Optional<HealthcareServiceDto> updateService(String authorizationHeader, String serviceId, CreateHealthcareServiceDto serviceDto) {
        String providerEmail = jwtUtils.getEmailFromHeader(authorizationHeader);
        
        return repository.findById(serviceId)
                .filter(entity -> entity.getProviderEmail().equals(providerEmail))
                .map(entity -> {
                    entity.setServiceName(serviceDto.getServiceName());
                    entity.setDescription(serviceDto.getDescription());
                    entity.setCategory(serviceDto.getCategory());
                    entity.setPrice(serviceDto.getPrice());
                    entity.setDurationMinutes(serviceDto.getDurationMinutes());
                    entity.setLocation(serviceDto.getLocation());
                    entity.setTags(serviceDto.getTags());
                    entity.setIsActive(serviceDto.getIsActive());
                    entity.setUpdatedAt(LocalDateTime.now());
                    
                    return HealthcareServiceDto.from(repository.save(entity));
                });
    }

    @Override
    public boolean deleteService(String authorizationHeader, String serviceId) {
        String providerEmail = jwtUtils.getEmailFromHeader(authorizationHeader);
        
        return repository.findById(serviceId)
                .filter(entity -> entity.getProviderEmail().equals(providerEmail))
                .map(entity -> {
                    entity.setIsActive(false); // Soft delete
                    entity.setUpdatedAt(LocalDateTime.now());
                    repository.save(entity);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<HealthcareServiceDto> getServicesByProvider(String providerEmail) {
        return repository.findByProviderEmailAndIsActive(providerEmail, true)
                .stream()
                .map(HealthcareServiceDto::from)
                .toList();
    }

    private String extractProviderName(String email) {
        // Simple extraction - in production, fetch from user/profile service
        return email.split("@")[0].replace(".", " ");
    }
}