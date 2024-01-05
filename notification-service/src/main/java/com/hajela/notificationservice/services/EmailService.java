package com.hajela.notificationservice.services;

import com.hajela.commons.messaging.ForgotPasswordMessage;
import com.hajela.commons.messaging.UserActivationMessage;

public interface EmailService {

    void sendActivationEmail(UserActivationMessage user);

    void sendForgotPasswordEmail(ForgotPasswordMessage forgotPasswordMessage);

}
