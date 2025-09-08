package com.example.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingStatusUpdateDto {
    private String bookingId;
    private String status;
    private String message;
    private LocalDateTime timestamp;
}