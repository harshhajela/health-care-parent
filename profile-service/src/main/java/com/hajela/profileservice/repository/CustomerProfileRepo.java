package com.hajela.profileservice.repository;

import com.hajela.profileservice.entity.CustomerProfileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerProfileRepo extends MongoRepository<CustomerProfileEntity, String> {

    public Optional<CustomerProfileEntity> findByEmail(String email);
}
