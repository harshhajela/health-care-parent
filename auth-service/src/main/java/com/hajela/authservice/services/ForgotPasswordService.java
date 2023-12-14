package com.hajela.authservice.services;

import com.hajela.authservice.entities.ForgotPasswordEntity;
import com.hajela.authservice.entities.UserEntity;

import java.util.Optional;

public interface ForgotPasswordService {

    ForgotPasswordEntity createForgotPasswordForUser(UserEntity user);

    Optional<ForgotPasswordEntity> getForgotPasswordEntityByToken(String token);

    ForgotPasswordEntity checkValidity(ForgotPasswordEntity forgotPasswordEntity);

    void updateResetPassword(ForgotPasswordEntity forgotPasswordEntity);
}
