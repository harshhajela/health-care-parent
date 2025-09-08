package com.hajela.products.dto;

import lombok.Data;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateHealthcareServiceDto {
    
    @NotBlank(message = "Service name is required")
    @Size(max = 100, message = "Service name must not exceed 100 characters")
    private String serviceName;
    
    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    @NotBlank(message = "Category is required")
    private String category;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
    
    @NotNull(message = "Duration is required")
    @Min(value = 15, message = "Duration must be at least 15 minutes")
    @Max(value = 480, message = "Duration must not exceed 8 hours")
    private Integer durationMinutes;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    private List<String> tags;
    
    @NotNull(message = "Active status is required")
    private Boolean isActive = true;
}