package com.hajela.notificationservice.services.impl;

import com.hajela.commons.messaging.ForgotPasswordMessage;
import com.hajela.commons.messaging.UserActivationMessage;
import com.hajela.notificationservice.services.EmailService;
import com.hajela.notificationservice.services.EmailTemplates;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${email.enabled}")
    private boolean enabled;

    @Value("${email.user.activation-link}")
    private String activationLink;

    @Value("${email.user.forgot-password-link}")
    private String forgotPasswordLink;

    private final JavaMailSender mailSender;


    @Override
    public void sendActivationEmail(UserActivationMessage user) {
        if (this.enabled) {
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                helper.setTo(user.getEmail());
                helper.setText(generateActivationText(user.getActivationToken()), true);
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
    public void sendForgotPasswordEmail(ForgotPasswordMessage forgotPasswordMessage) {
        if (this.enabled) {
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                helper.setTo(forgotPasswordMessage.getEmail());
                helper.setText(generateResetPasswordText(forgotPasswordMessage.getToken()), true);
                helper.setSubject("Reset your password");
                helper.setFrom("hello@helpinghands.com");
                mailSender.send(mimeMessage);
                log.info("Reset - Forgot password email sent!");
            } catch (MessagingException e) {
                log.error("Error while sending activation email", e);
            }

        }
    }

    private String generateResetPasswordText(String token) {
        String resetPasswordLink = String.format(this.forgotPasswordLink, token);
        return String.format(EmailTemplates.RESET_PASSWORD_EMAIL_TEMPLATE, resetPasswordLink);
    }

    private String generateActivationText(String activationToken) {
        String link = String.format(this.activationLink, activationToken);
        return String.format(EmailTemplates.ACTIVATION_EMAIL_TEMPLATE, link);
    }
}
