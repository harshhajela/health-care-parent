package com.hajela.authservice.services.impl;

import com.hajela.authservice.dto.*;
import com.hajela.authservice.entities.ForgotPasswordEntity;
import com.hajela.authservice.entities.RefreshTokenEntity;
import com.hajela.authservice.entities.UserActivationEntity;
import com.hajela.authservice.entities.UserEntity;
import com.hajela.authservice.exceptions.ActivateAccountException;
import com.hajela.authservice.exceptions.ForgotPasswordException;
import com.hajela.authservice.exceptions.RefreshTokenNotFound;
import com.hajela.authservice.exceptions.ResetForgotPasswordException;
import com.hajela.authservice.messaging.MessageProducerService;
import com.hajela.authservice.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    public static final String ACCESS = "ACCESS";

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final UserActivationService userActivationService;
    private final ForgotPasswordService forgotPasswordService;
    private final JwtUtils jwtUtils;
    private final MessageProducerService messageProducerService;

    public AuthResponse login(AuthRequest authRequest) {
        UserEntity userEntity = userService.login(authRequest);
        log.info("Login successful! {}", userEntity);

        String accessToken = jwtUtils.generate(userEntity, ACCESS);
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(userEntity);
        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshTokenEntity::getUser)
                .map(userEntity -> {
                    String accessToken = jwtUtils.generate(userEntity, ACCESS);
                    return AuthResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshTokenRequest.getRefreshToken())
                            .build();
                }).orElseThrow(() -> new
                        RefreshTokenNotFound(refreshTokenRequest.getRefreshToken()));
    }

    public AuthResponse activateAccount(ActivateAccountDto activateAccountDto) {
        return userActivationService.getUserActivationEntityByToken(activateAccountDto.getToken())
                .map(userActivationService::verifyExpirationAndTokenNotUsed)
                .map(userActivationService::updateActivationEntityConfirmedTime)
                .map(UserActivationEntity::getUser)
                .map(userService::updateUserStatusToActivated)
                .map(userEntity -> {
                    String accessToken = jwtUtils.generate(userEntity, ACCESS);
                    RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(userEntity);
                    return AuthResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken.getToken())
                            .build();
                }).orElseThrow(() -> new ActivateAccountException("Activation token not found"));
    }

    public void forgotPassword(ForgotPasswordDto forgotPasswordDto) {
        userService.findUserByEmail(forgotPasswordDto.getEmail())
                .ifPresentOrElse(user -> {
                    userService.updateUserStatusToForgotPassword(user);
                    ForgotPasswordEntity forgotPasswordEntity = forgotPasswordService.createForgotPasswordForUser(user);
                    messageProducerService.sendForgotPassword(forgotPasswordEntity);
                }, () -> {
                    throw new ForgotPasswordException("Could not find user with email:" + forgotPasswordDto.getEmail());
                });

    }

    public void resetUserPassword(ResetPasswordDto resetPasswordDto) {
        if (Objects.equals(resetPasswordDto.getNewPassword(), resetPasswordDto.getConfirmNewPassword())) {
            forgotPasswordService.getForgotPasswordEntityByToken(resetPasswordDto.getToken())
                    .map(forgotPasswordService::checkValidity)
                    .map(forgotPasswordEntity -> {
                        userService.updateUserStatusToActivated(forgotPasswordEntity.getUser());
                        userService.resetUserPassword(forgotPasswordEntity.getUser(), resetPasswordDto);
                        forgotPasswordService.updateResetPassword(forgotPasswordEntity);
                        return forgotPasswordEntity;
                    })
                    .orElseThrow(() ->
                         new ResetForgotPasswordException("No reset password entity found by token" + resetPasswordDto.getToken()));
        } else {
            throw new ResetForgotPasswordException("New password and confirm password do not match.");
        }
    }
}
