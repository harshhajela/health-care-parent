package com.hajela.notificationservice.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hajela.commons.messaging.ForgotPasswordMessage;
import com.hajela.commons.messaging.UserActivationMessage;
import com.hajela.notificationservice.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Profile("artemis")
@RequiredArgsConstructor
public class UserListenerArtemis {

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
