package com.hajela.authservice.repo;


import com.hajela.authservice.entities.RefreshTokenEntity;
import com.hajela.authservice.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Integer> {
    void deleteAllByUser(UserEntity userEntity);

    Optional<RefreshTokenEntity> findByToken(String token);
}
