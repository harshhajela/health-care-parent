package com.hajela.authservice.services.impl;


import com.hajela.authservice.entities.RefreshTokenEntity;
import com.hajela.authservice.entities.UserEntity;
import com.hajela.authservice.exceptions.RefreshTokenExpired;
import com.hajela.authservice.repo.RefreshTokenRepository;
import com.hajela.authservice.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenEntity createRefreshToken(UserEntity userEntity) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .token(UUID.randomUUID().toString())
                .user(userEntity)
                .expiryDate(Instant.now().plusSeconds(600))
                .build();
        return refreshTokenRepository.save(refreshTokenEntity);
    }

    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(token);
            throw new RefreshTokenExpired(token.getToken());
        }
        return token;
    }
}
