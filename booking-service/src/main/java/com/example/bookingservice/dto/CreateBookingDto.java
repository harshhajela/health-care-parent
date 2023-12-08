package com.example.bookingservice.dto;

import com.example.bookingservice.model.BookingEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingDto {
    private Integer customerId;
    private Integer careProviderId;

    public static BookingEntity from(CreateBookingDto dto) {
        return BookingEntity.builder()
                .customerId(dto.getCustomerId())
                .careProviderId(dto.getCareProviderId())
                .created(LocalDateTime.now())
                .build();
    }
}
