package com.hajela.authservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hajela.authservice.entities.ForgotPasswordEntity;
import com.hajela.authservice.entities.UserActivationEntity;

import com.hajela.commons.messaging.ForgotPasswordMessage;
import com.hajela.commons.messaging.UserActivationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("artemis")
@RequiredArgsConstructor
public class ArtemisMessageProducer implements MessageProducer {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Value("${queue-name.user-activation}")
    private String activationQueueName;

    @Value("${queue-name.user-forgot-password}")
    private String forgotPasswordQueueName;

    @Async
    @Override
    public void sendUserActivationMessage(UserActivationEntity userActivationEntity) {
        UserActivationMessage userActivationMessage = UserActivationMessage.builder()
                .email(userActivationEntity.getUser().getEmail())
                .role(userActivationEntity.getUser().getRole().getName().getRoleName())
                .activationToken(userActivationEntity.getToken())
                .build();
        String jsonMessage = "";
        try {
            jsonMessage = objectMapper.writeValueAsString(userActivationMessage);
        } catch (JsonProcessingException e) {
            log.error("Error while converting message", e);
        }
        jmsTemplate.convertAndSend(activationQueueName, jsonMessage);
        log.info("User registration message sent: {}", jsonMessage);
    }

    @Override
    public void sendForgotPassword(ForgotPasswordEntity forgotPasswordEntity) {
        ForgotPasswordMessage message = ForgotPasswordMessage.builder()
                .email(forgotPasswordEntity.getUser().getEmail())
                .token(forgotPasswordEntity.getToken())
                .build();
        String jsonMessage = "";
        try {
            jsonMessage = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            log.error("Error while converting message", e);
        }
        jmsTemplate.convertAndSend(forgotPasswordQueueName, jsonMessage);
        log.info("Forgot password message sent: {}", jsonMessage);
    }

}
