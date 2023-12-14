package com.hajela.authservice.services;

import com.hajela.authservice.entities.RefreshTokenEntity;
import com.hajela.authservice.entities.UserEntity;

import java.util.Optional;

public interface RefreshTokenService {

    public RefreshTokenEntity createRefreshToken(UserEntity userEntity);

    public Optional<RefreshTokenEntity> findByToken(String token);

    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token);
}
