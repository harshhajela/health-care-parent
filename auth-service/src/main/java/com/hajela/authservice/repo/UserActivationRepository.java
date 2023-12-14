package com.hajela.authservice.repo;

import com.hajela.authservice.entities.UserActivationEntity;
import com.hajela.authservice.entities.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserActivationRepository extends JpaRepository<UserActivationEntity, Long> {

    @EntityGraph(attributePaths = {"user"})
    Optional<UserActivationEntity> findByUser(UserEntity user);

    @EntityGraph(attributePaths = {"user"})
    Optional<UserActivationEntity> findByToken(String token);
}
