package com.hajela.notificationservice.services;

import com.hajela.notificationservice.dto.ForgotPasswordMessage;
import com.hajela.notificationservice.dto.UserActivationMessage;

public interface EmailService {

    void sendActivationEmail(UserActivationMessage user);

    void sendForgotPasswordEmail(ForgotPasswordMessage forgotPasswordMessage);

}
