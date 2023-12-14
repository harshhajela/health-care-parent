package com.hajela.authservice.services;

import com.hajela.authservice.entities.ForgotPasswordEntity;
import com.hajela.authservice.entities.UserEntity;

import java.util.Optional;

public interface EmailService {

    void sendActivationEmail(UserEntity user);

    void sendForgotPassword(ForgotPasswordEntity forgotPasswordEntity);
}
