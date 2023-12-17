package com.example.bookingservice.repository;


import com.example.bookingservice.entities.BookingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<BookingEntity, String> {

    List<BookingEntity> findAllByCustomer_EmailOrderByCreatedAtDesc(String customerEmail);
    List<BookingEntity> findAllByProvider_EmailOrderByCreatedAtDesc(String providerEmail);

}
