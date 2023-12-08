package com.example.bookingservice.controller;

import com.example.bookingservice.dto.BookingDto;
import com.example.bookingservice.dto.CreateBookingDto;
import com.example.bookingservice.model.BookingEntity;
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
    public ResponseEntity<BookingDto> getBooking(@PathVariable(value = "bookingId") Integer bookingId) {
        var booking = bookingService.getBooking(bookingId);
        return booking.map(entity -> ResponseEntity.ok(BookingEntity.from(entity)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping(value = "/history/{customerId}")
    public ResponseEntity<List<BookingDto>> getCustomerBookingHistory(@PathVariable(value = "customerId") Integer customerId) {
        return ResponseEntity.ok(bookingService.getCustomerBookingHistory(customerId));
    }

    @PostMapping
    public ResponseEntity<BookingDto> saveBookingForCustomer(@RequestBody CreateBookingDto bookingDto) {
        var newBooking = bookingService.createNewBooking(bookingDto);
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
}
