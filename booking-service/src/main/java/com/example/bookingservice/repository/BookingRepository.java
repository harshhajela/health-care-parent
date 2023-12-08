package com.example.bookingservice.repository;


import com.example.bookingservice.model.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {

    List<BookingEntity> findAllByCustomerIdOrderByCreatedDesc(Integer customerId);
}
