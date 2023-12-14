package com.hajela.authservice.services.impl;

import com.hajela.authservice.entities.ForgotPasswordEntity;
import com.hajela.authservice.entities.UserEntity;
import com.hajela.authservice.services.EmailService;
import com.hajela.authservice.services.UserActivationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${user.activation-link}")
    private String activationLink;

    @Value("${user.forgot-password-link}")
    private String forgotPasswordLink;

    private final JavaMailSender mailSender;
    private final UserActivationService activationService;

    @Async
    public void sendActivationEmail(UserEntity user) {
        var optionalUserActivation = activationService.getUserActivationEntity(user);
        if (optionalUserActivation.isPresent()) {
            var userActivation = optionalUserActivation.get();
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
                helper.setTo(user.getEmail());
                helper.setText(generateActivationText(userActivation.getToken()));
                helper.setSubject("Confirm your email");
                helper.setFrom("hello@helpinghands.com");
                mailSender.send(mimeMessage);
                log.info("Activation email sent!");
            } catch (MessagingException e) {
                log.error("Error while sending activation email", e);
            }
        }
    }

    @Override
    @Async
    public void sendForgotPassword(ForgotPasswordEntity forgotPasswordEntity) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setTo(forgotPasswordEntity.getUser().getEmail());
            helper.setText(generateForgotPasswordText(forgotPasswordEntity.getToken()));
            helper.setSubject("Reset your password");
            helper.setFrom("hello@helpinghands.com");
            mailSender.send(mimeMessage);
            log.info("Reset password email sent!");
        } catch (MessagingException e) {
            log.error("Error while sending reset password email", e);
        }
    }

    private String generateForgotPasswordText(String token) {
        return String.format(forgotPasswordLink, token);
    }

    private String generateActivationText(String token) {
        return String.format(activationLink, token);
    }
}
