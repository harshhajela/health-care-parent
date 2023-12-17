package com.example.bookingservice.entities;

import com.example.bookingservice.dto.BookingDto;
import com.example.bookingservice.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "customer-booking")
public class BookingEntity {
    @Id
    private String bookingId;
    private UserDto customer;
    private UserDto provider;
    private LocalDateTime createdAt;
    private LocalDateTime bookingDate;
    private BookingStatus bookingStatus;
    private List<String> servicesBooked;

    public static BookingDto from(BookingEntity entity) {
        return BookingDto.builder()
                .bookingId(entity.getBookingId())
                .customerEmail(entity.getCustomer().getEmail())
                .providerEmail(entity.getProvider().getEmail())
                .createdAt(entity.getCreatedAt())
                .bookingDate(entity.getBookingDate())
                .bookingStatus(entity.getBookingStatus())
                .servicesBooked(entity.getServicesBooked())
                .build();
    }
}
