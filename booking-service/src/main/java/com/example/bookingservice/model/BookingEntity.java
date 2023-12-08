package com.example.bookingservice.model;

import com.example.bookingservice.dto.BookingDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(name = "booking")
@AllArgsConstructor
@NoArgsConstructor
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingId;
    private Integer customerId;
    private Integer careProviderId;
    private LocalDateTime created;

    public static BookingDto from(BookingEntity entity) {
        return BookingDto.builder()
                .bookingId(entity.getBookingId())
                .customerId(entity.getCustomerId())
                .careProviderId(entity.getCareProviderId())
                .created(entity.getCreated())
                .build();
    }
}
