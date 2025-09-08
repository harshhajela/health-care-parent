package com.hajela.products.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "healthcare-services")
public class HealthcareServiceEntity {
    
    @Id
    private String id;
    
    @Indexed
    private String serviceName;
    
    private String description;
    
    @Indexed
    private String category;
    
    private BigDecimal price;
    
    private Integer durationMinutes;
    
    @Indexed
    private String providerEmail;
    
    private String providerName;
    
    @Indexed
    private String location;
    
    private List<String> tags;
    
    @Indexed
    private Boolean isActive;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}