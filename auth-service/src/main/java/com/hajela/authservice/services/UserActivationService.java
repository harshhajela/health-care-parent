package com.hajela.authservice.services;

import com.hajela.authservice.entities.UserActivationEntity;
import com.hajela.authservice.entities.UserEntity;

import java.util.Optional;

public interface UserActivationService {

    void createNewActivationCode(UserEntity user);

    Optional<UserActivationEntity> getUserActivationEntity(UserEntity user);


    Optional<UserActivationEntity> getUserActivationEntityByToken(String token);

    UserActivationEntity verifyExpirationAndTokenNotUsed(UserActivationEntity activationEntity);

    UserActivationEntity updateActivationEntityConfirmedTime(UserActivationEntity activationEntity);
}
