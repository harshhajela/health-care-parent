package com.hajela.authservice.repo;

import com.hajela.authservice.entities.ForgotPasswordEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordEntity, Long> {

    @EntityGraph(attributePaths = {"user"})
    Optional<ForgotPasswordEntity> findByToken(String token);
}
