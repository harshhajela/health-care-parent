package com.example.bookingservice.dto;

import com.example.bookingservice.entities.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingDto {
    private String bookingId;
    private BookingStatus bookingStatus;
}
