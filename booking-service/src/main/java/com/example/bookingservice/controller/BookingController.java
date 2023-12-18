package com.example.bookingservice.controller;

import com.example.bookingservice.dto.BookingDto;
import com.example.bookingservice.dto.CreateBookingDto;
import com.example.bookingservice.dto.UpdateBookingDto;
import com.example.bookingservice.entities.BookingEntity;
import com.example.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping(value = "/{bookingId}")
    public ResponseEntity<BookingDto> getBooking(@PathVariable(value = "bookingId") String bookingId) {
        var booking = bookingService.getBooking(bookingId);
        return booking.map(entity -> ResponseEntity.ok(BookingEntity.from(entity)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping(value = "/history")
    public ResponseEntity<List<BookingDto>> getBookingHistory(
            @RequestHeader(name = "Authorization") String authorizationHeader) {
        return ResponseEntity.ok(bookingService.getBookingHistory(authorizationHeader));
    }

    @PostMapping
    public ResponseEntity<BookingDto> createNewBooking(@RequestHeader(name = "Authorization") String authorizationHeader,
                                                       @RequestBody CreateBookingDto bookingDto) {
        var newBooking = bookingService.createNewBooking(authorizationHeader, bookingDto);
        if (newBooking.isEmpty()) {
            return ResponseEntity.badRequest().build();
        } else {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{bookingId}")
                    .build(newBooking.get().getBookingId());
            return ResponseEntity.created(uri)
                    .body(BookingEntity.from(newBooking.get()));
        }
    }

    @PatchMapping("update")
    public ResponseEntity<BookingDto> updateBookingStatus(@RequestBody UpdateBookingDto updateBookingDto) {
        return  ResponseEntity.ok(
                bookingService.updateBookingStatus(
                        updateBookingDto.getBookingId(),
                        updateBookingDto.getBookingStatus()));
    }
}
