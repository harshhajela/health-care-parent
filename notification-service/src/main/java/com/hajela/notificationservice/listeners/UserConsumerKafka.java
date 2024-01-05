package com.hajela.notificationservice.listeners;


import com.hajela.commons.messaging.ForgotPasswordMessage;
import com.hajela.commons.messaging.UserActivationMessage;
import com.hajela.notificationservice.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("kafka")
@RequiredArgsConstructor
public class UserConsumerKafka {

    public static final String USER_ACTIVATION_TOPIC = "USER-ACTIVATION-TOPIC";
    public static final String FORGOT_PASSWORD_TOPIC = "FORGOT-PASSWORD-TOPIC";
    public static final String GROUP_ID = "NOTIFICATION-SERVICE-1";

    private final EmailService emailService;

    @KafkaListener(topics = USER_ACTIVATION_TOPIC, groupId = GROUP_ID)
    public void consumeUserActivation(UserActivationMessage userActivationMessage) {
        log.info("RECEIVED via KAFKA: {}", userActivationMessage);
        emailService.sendActivationEmail(userActivationMessage);
    }

    @KafkaListener(topics = FORGOT_PASSWORD_TOPIC, groupId = GROUP_ID)
    public void consumeForgotPassword(ForgotPasswordMessage forgotPasswordMessage) {
        log.info("RECEIVED via KAFKA: {}", forgotPasswordMessage);
        emailService.sendForgotPasswordEmail(forgotPasswordMessage);
    }
}
