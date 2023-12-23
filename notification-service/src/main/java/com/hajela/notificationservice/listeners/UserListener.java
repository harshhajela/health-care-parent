package com.hajela.notificationservice.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hajela.notificationservice.dto.ForgotPasswordMessage;
import com.hajela.notificationservice.dto.UserActivationMessage;
import com.hajela.notificationservice.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserListener {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    @JmsListener(destination = "${queue-name.user-activation}")
    public void handleUserActivationEvent(String message) throws JsonProcessingException {

        UserActivationMessage userActivationMessage = objectMapper.readValue(message, UserActivationMessage.class);
        emailService.sendActivationEmail(userActivationMessage);
    }

    @JmsListener(destination = "${queue-name.user-forgot-password}")
    public void handleForgotPasswordEvent(String message) throws JsonProcessingException {
        ForgotPasswordMessage forgotPasswordMessage = objectMapper.readValue(message, ForgotPasswordMessage.class);
        emailService.sendForgotPasswordEmail(forgotPasswordMessage);
    }
}
