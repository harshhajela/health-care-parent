package com.example.bookingservice.dto;


import com.example.bookingservice.entities.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingHistoryResponseDto {

    private List<BookingDto> bookingDtoList;
    private List<BookingDto> upcomingBookings;
    private List<BookingDto> pastBookings;
    private List<BookingDto> cancelledBookings;

    public BookingHistoryResponseDto(List<BookingDto> bookingDtoList) {
        this.bookingDtoList = bookingDtoList;
        this.upcomingBookings = createUpcomingBookings(bookingDtoList);
        this.pastBookings = createPastBookings(bookingDtoList);
        this.cancelledBookings = createCancelledBookings(bookingDtoList);
    }

    private List<BookingDto> createCancelledBookings(List<BookingDto> bookingDtoList) {
        return bookingDtoList.stream()
                .filter(bookingDto -> bookingDto.getBookingStatus() == BookingStatus.BOOKING_CANCELLED)
                .sorted(Comparator.comparing(BookingDto::getCreatedAt).reversed())
                .toList();
    }

    private List<BookingDto> createPastBookings(List<BookingDto> bookingDtoList) {
        return bookingDtoList.stream()
                .filter(bookingDto -> bookingDto.getBookingStatus() == BookingStatus.BOOKING_COMPLETED)
                .sorted(Comparator.comparing(BookingDto::getCreatedAt).reversed())
                .toList();
    }

    private List<BookingDto> createUpcomingBookings(List<BookingDto> bookingDtoList) {
        return bookingDtoList.stream()
                .filter(bookingDto -> bookingDto.getBookingStatus() == BookingStatus.BOOKING_CREATED ||
                        bookingDto.getBookingStatus() == BookingStatus.BOOKING_ACCEPTED)
                .sorted(Comparator.comparing(BookingDto::getCreatedAt).reversed())
                .toList();
    }
}
