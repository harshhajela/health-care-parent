package com.hajela.authservice.messaging;

import com.hajela.authservice.entities.ForgotPasswordEntity;
import com.hajela.authservice.entities.UserActivationEntity;
import com.hajela.commons.messaging.ForgotPasswordMessage;
import com.hajela.commons.messaging.UserActivationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageProducer implements MessageProducer {

    public static final String USER_ACTIVATION_TOPIC = "USER-ACTIVATION-TOPIC";
    public static final String FORGOT_PASSWORD_TOPIC = "FORGOT-PASSWORD-TOPIC";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendUserActivationMessage(UserActivationEntity userActivationEntity) {

        UserActivationMessage userActivationMessage = UserActivationMessage.builder()
                .email(userActivationEntity.getUser().getEmail())
                .role(userActivationEntity.getUser().getRole().getName().getRoleName())
                .activationToken(userActivationEntity.getToken())
                .build();
        try {
            kafkaTemplate.send(USER_ACTIVATION_TOPIC, userActivationMessage);
            log.info("User registration message sent: {}", userActivationMessage);
        } catch (Exception e) {
            log.error("ERROR While producing kafka user activation", e);
        }
    }

    @Override
    public void sendForgotPassword(ForgotPasswordEntity forgotPasswordEntity) {

        ForgotPasswordMessage message = ForgotPasswordMessage.builder()
                .email(forgotPasswordEntity.getUser().getEmail())
                .token(forgotPasswordEntity.getToken())
                .build();
        try {
            kafkaTemplate.send(FORGOT_PASSWORD_TOPIC, message);
            log.info("Forgot password message sent: {}", message);
        } catch (Exception e) {
            log.error("ERROR While producing kafka forgot password", e);
        }
    }
}
