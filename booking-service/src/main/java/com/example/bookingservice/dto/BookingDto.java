package com.example.bookingservice.dto;

import com.example.bookingservice.entities.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private String bookingId;
    private String customerEmail;
    private String providerEmail;
    private LocalDateTime createdAt;
    private LocalDateTime bookingDate;
    private BookingStatus bookingStatus;
    private List<String> servicesBooked;
}
