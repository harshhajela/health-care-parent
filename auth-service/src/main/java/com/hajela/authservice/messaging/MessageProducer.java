package com.hajela.authservice.messaging;

import com.hajela.authservice.entities.ForgotPasswordEntity;
import com.hajela.authservice.entities.UserActivationEntity;

public interface MessageProducer {

    void sendUserActivationMessage(UserActivationEntity userActivationEntity);

    void sendForgotPassword(ForgotPasswordEntity forgotPasswordEntity);
}
