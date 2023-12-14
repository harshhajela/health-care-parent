package com.hajela.authservice.services.impl;

import com.hajela.authservice.entities.UserActivationEntity;
import com.hajela.authservice.entities.UserEntity;
import com.hajela.authservice.exceptions.ActivateAccountException;
import com.hajela.authservice.repo.UserActivationRepository;
import com.hajela.authservice.services.UserActivationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserActivationServiceImpl implements UserActivationService {

    public static final int ACTIVATION_CODE_EXPIRY = 15;


    private final UserActivationRepository userActivationRepository;

    @Override
    public void createNewActivationCode(UserEntity user) {
        UserActivationEntity activationEntity = UserActivationEntity.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(ACTIVATION_CODE_EXPIRY))
                .build();

        userActivationRepository.save(activationEntity);
    }

    @Override
    public Optional<UserActivationEntity> getUserActivationEntity(UserEntity user) {
        return userActivationRepository.findByUser(user);
    }

    @Override
    public Optional<UserActivationEntity> getUserActivationEntityByToken(String token) {
        return userActivationRepository.findByToken(token);
    }

    @Override
    public UserActivationEntity verifyExpirationAndTokenNotUsed(UserActivationEntity activationEntity) {
        if (!activationEntity.getExpiredAt().isAfter(LocalDateTime.now())) {
            log.info("Activation email expired! UserId={} ExpiredAt={}",
                    activationEntity.getUser().getUserId(), activationEntity.getExpiredAt());
            throw new ActivateAccountException("Activation link expired");
        } else if (activationEntity.getConfirmedAt() != null) {
            log.info("Activation email already used! UserId={} ConfirmedAt={}",
                    activationEntity.getUser().getUserId(), activationEntity.getConfirmedAt());
            throw new ActivateAccountException("Activation link already used");
        }
        return activationEntity;
    }

    @Override
    public UserActivationEntity updateActivationEntityConfirmedTime(UserActivationEntity activationEntity) {
        activationEntity.setConfirmedAt(LocalDateTime.now());
        return userActivationRepository.save(activationEntity);
    }

}
