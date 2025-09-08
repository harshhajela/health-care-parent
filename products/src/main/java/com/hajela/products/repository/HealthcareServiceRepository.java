package com.hajela.products.repository;

import com.hajela.products.entity.HealthcareServiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthcareServiceRepository extends MongoRepository<HealthcareServiceEntity, String> {
    
    Page<HealthcareServiceEntity> findByIsActive(Boolean isActive, Pageable pageable);
    
    Page<HealthcareServiceEntity> findByCategoryAndIsActive(String category, Boolean isActive, Pageable pageable);
    
    Page<HealthcareServiceEntity> findByLocationAndIsActive(String location, Boolean isActive, Pageable pageable);
    
    Page<HealthcareServiceEntity> findByCategoryAndLocationAndIsActive(String category, String location, Boolean isActive, Pageable pageable);
    
    Optional<HealthcareServiceEntity> findByIdAndIsActive(String id, Boolean isActive);
    
    List<HealthcareServiceEntity> findByCategoryAndIsActive(String category, Boolean isActive);
    
    List<HealthcareServiceEntity> findByProviderEmailAndIsActive(String providerEmail, Boolean isActive);
    
    Page<HealthcareServiceEntity> findByServiceNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndIsActive(
            String serviceName, String description, Boolean isActive, Pageable pageable);
}