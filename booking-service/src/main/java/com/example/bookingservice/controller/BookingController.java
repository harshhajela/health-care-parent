package com.example.bookingservice.controller;

import com.example.bookingservice.dto.BookingDto;
import com.example.bookingservice.dto.BookingHistoryResponseDto;
import com.example.bookingservice.dto.CreateBookingDto;
import com.example.bookingservice.dto.UpdateBookingDto;
import com.example.bookingservice.entities.BookingEntity;
import com.example.bookingservice.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/bookings")
@Tag(name = "Booking Management", description = "Healthcare appointment booking operations")
public class BookingController {

    private final BookingService bookingService;

    @Operation(summary = "Get booking by ID", description = "Retrieve booking details by booking ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Booking found", 
            content = @Content(schema = @Schema(implementation = BookingDto.class))),
        @ApiResponse(responseCode = "204", description = "Booking not found")
    })
    @GetMapping(value = "/{bookingId}")
    public ResponseEntity<BookingDto> getBooking(
        @Parameter(description = "Booking ID", required = true)
        @PathVariable(value = "bookingId") String bookingId) {
        var booking = bookingService.getBooking(bookingId);
        return booking.map(entity -> ResponseEntity.ok(BookingEntity.from(entity)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Operation(summary = "Get booking history", description = "Get booking history for customer or provider")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Booking history retrieved", 
            content = @Content(schema = @Schema(implementation = BookingHistoryResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "Invalid token")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = "/history")
    public ResponseEntity<BookingHistoryResponseDto> getBookingHistory(
        @Parameter(description = "JWT Bearer token", required = true)
        @RequestHeader(name = "Authorization") String authorizationHeader) {
        List<BookingDto> bookingHistory = bookingService.getBookingHistory(authorizationHeader);
        BookingHistoryResponseDto responseDto = new BookingHistoryResponseDto(bookingHistory);
        log.info("{}", responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Create new booking", description = "Create a new healthcare appointment booking")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Booking created successfully", 
            content = @Content(schema = @Schema(implementation = BookingDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid booking data"),
        @ApiResponse(responseCode = "401", description = "Invalid token")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<BookingDto> createNewBooking(
        @Parameter(description = "JWT Bearer token", required = true)
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @Parameter(description = "Booking details", required = true)
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

    @Operation(summary = "Update booking status", description = "Update the status of an existing booking")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Booking status updated", 
            content = @Content(schema = @Schema(implementation = BookingDto.class))),
        @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PatchMapping("update")
    public ResponseEntity<BookingDto> updateBookingStatus(
        @Parameter(description = "Booking status update details", required = true)
        @RequestBody UpdateBookingDto updateBookingDto) {
        return  ResponseEntity.ok(
                bookingService.updateBookingStatus(
                        updateBookingDto.getBookingId(),
                        updateBookingDto.getBookingStatus()));
    }
}
