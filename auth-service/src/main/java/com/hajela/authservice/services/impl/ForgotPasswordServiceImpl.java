package com.hajela.authservice.services.impl;

import com.hajela.authservice.entities.ForgotPasswordEntity;
import com.hajela.authservice.entities.UserEntity;
import com.hajela.authservice.exceptions.ResetForgotPasswordException;
import com.hajela.authservice.repo.ForgotPasswordRepository;
import com.hajela.authservice.services.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    public static final int EXPIRY_MINUTES = 15;

    private final ForgotPasswordRepository forgotPasswordRepository;

    @Override
    public ForgotPasswordEntity createForgotPasswordForUser(UserEntity user) {
        ForgotPasswordEntity forgotPasswordEntity = ForgotPasswordEntity.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(EXPIRY_MINUTES))
                .build();
        return forgotPasswordRepository.save(forgotPasswordEntity);
    }

    @Override
    public Optional<ForgotPasswordEntity> getForgotPasswordEntityByToken(String token) {
        return forgotPasswordRepository.findByToken(token);
    }

    @Override
    public ForgotPasswordEntity checkValidity(ForgotPasswordEntity forgotPasswordEntity) {
        if (forgotPasswordEntity.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new ResetForgotPasswordException("Reset password link has expired");
        } else if (forgotPasswordEntity.getConfirmedAt() != null) {
            throw new ResetForgotPasswordException("Reset password link is already used");
        }
        return forgotPasswordEntity;
    }

    @Override
    public void updateResetPassword(ForgotPasswordEntity forgotPasswordEntity) {
        forgotPasswordEntity.setConfirmedAt(LocalDateTime.now());
        forgotPasswordRepository.save(forgotPasswordEntity);
    }
}
