package com.hajela.profileservice.repository;

import com.hajela.profileservice.entity.ProviderProfileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderProfileRepo extends MongoRepository<ProviderProfileEntity, String> {

    Optional<ProviderProfileEntity> findByEmail(String email);
}
