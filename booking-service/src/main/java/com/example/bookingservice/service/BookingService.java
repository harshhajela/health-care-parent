package com.example.bookingservice.service;

import com.example.bookingservice.client.UserServiceClient;
import com.example.bookingservice.dto.BookingDto;
import com.example.bookingservice.dto.CreateBookingDto;
import com.example.bookingservice.dto.UserDto;
import com.example.bookingservice.entities.BookingEntity;
import com.example.bookingservice.entities.BookingStatus;
import com.example.bookingservice.exceptions.BookingException;
import com.example.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    public static final String CUSTOMER = "CUSTOMER";
    public static final String PROVIDER = "PROVIDER";

    private final BookingRepository bookingRepository;
    private final UserServiceClient userClient;
    private final JwtUtils jwtUtils;

    public List<BookingDto> getBookingHistory(String authorization) {
        String role = jwtUtils.getRoleFromHeader(authorization);

        if (role.equalsIgnoreCase(CUSTOMER)) {
            String customerEmail = jwtUtils.getEmailFromHeader(authorization);
            return bookingRepository.findAllByCustomer_EmailOrderByCreatedAtDesc(customerEmail).stream()
                    .map(BookingEntity::from).toList();
        } else if (role.equalsIgnoreCase(PROVIDER)) {
            String providerEmail = jwtUtils.getEmailFromHeader(authorization);
            return bookingRepository.findAllByProvider_EmailOrderByCreatedAtDesc(providerEmail).stream()
                    .map(BookingEntity::from).toList();
        }
        return Collections.emptyList();
    }

    public Optional<BookingEntity> createNewBooking(String authorizationHeader, CreateBookingDto bookingDto) {
        String customerEmail = jwtUtils.getEmailFromHeader(authorizationHeader);

        Optional<UserDto> customerOptional = userClient.getUserByEmail(customerEmail);
        Optional<UserDto> providerOptional = userClient.getUserByEmail(bookingDto.getProviderEmail());
        if (customerOptional.isEmpty() || providerOptional.isEmpty()) {
            throw new BookingException("data.bot.found",
                    "Customer or Provider not found");
        }
        BookingEntity bookingEntity = BookingEntity.builder()
                .customer(customerOptional.get())
                .provider(providerOptional.get())
                .servicesBooked(bookingDto.getServicesBooked())
                .bookingDate(bookingDto.getBookingDate())
                .createdAt(LocalDateTime.now())
                .bookingStatus(BookingStatus.BOOKING_CREATED)
                .build();
        bookingRepository.save(bookingEntity);
        return Optional.of(bookingEntity);
    }

    public Optional<BookingEntity> getBooking(String bookingId) {
        return bookingRepository.findById(bookingId);
    }

    public BookingDto updateBookingStatus(String bookingId, BookingStatus action) {
        Optional<BookingEntity> entityOptional = bookingRepository.findById(bookingId);
        if (entityOptional.isEmpty()) {
            throw new BookingException("data.not.found", "Booking not found");
        }
        BookingEntity bookingEntity = entityOptional.get();
        bookingEntity.setBookingStatus(action);
        bookingEntity.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(bookingEntity);
        return BookingEntity.from(bookingEntity);
    }
}
