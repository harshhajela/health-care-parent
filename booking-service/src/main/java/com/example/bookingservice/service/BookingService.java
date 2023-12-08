package com.example.bookingservice.service;

import com.example.bookingservice.dto.BookingDto;
import com.example.bookingservice.dto.CreateBookingDto;
import com.example.bookingservice.model.BookingEntity;
import com.example.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    public List<BookingDto> getCustomerBookingHistory(Integer customerId) {
        return bookingRepository.findAllByCustomerIdOrderByCreatedDesc(customerId).stream()
                .map(BookingEntity::from).toList();
    }

    public Optional<BookingEntity> createNewBooking(CreateBookingDto bookingDto) {
        var entity = CreateBookingDto.from(bookingDto);
        bookingRepository.save(entity);
        return Optional.of(entity);
    }

    public Optional<BookingEntity> getBooking(Integer bookingId) {
        return bookingRepository.findById(bookingId);
    }
}
