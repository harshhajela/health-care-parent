package com.hajela.products.dto;

import com.hajela.products.entity.HealthcareServiceEntity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class HealthcareServiceDto {
    private String id;
    private String serviceName;
    private String description;
    private String category;
    private BigDecimal price;
    private Integer durationMinutes;
    private String providerEmail;
    private String providerName;
    private String location;
    private List<String> tags;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static HealthcareServiceDto from(HealthcareServiceEntity entity) {
        return HealthcareServiceDto.builder()
                .id(entity.getId())
                .serviceName(entity.getServiceName())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .price(entity.getPrice())
                .durationMinutes(entity.getDurationMinutes())
                .providerEmail(entity.getProviderEmail())
                .providerName(entity.getProviderName())
                .location(entity.getLocation())
                .tags(entity.getTags())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}