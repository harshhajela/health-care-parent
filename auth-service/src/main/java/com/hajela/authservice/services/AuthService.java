package com.hajela.authservice.services;

import com.hajela.authservice.dto.AuthRequest;
import com.hajela.authservice.dto.AuthResponse;
import com.hajela.authservice.dto.RefreshTokenRequest;
import com.hajela.authservice.entities.RefreshTokenEntity;
import com.hajela.authservice.entities.UserEntity;
import com.hajela.authservice.exceptions.RefreshTokenNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j @Service @RequiredArgsConstructor
public class AuthService {
    public static final String ACCESS = "ACCESS";
    public static final String REFRESH = "REFRESH";

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

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
                    AuthResponse authResponse = AuthResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshTokenRequest.getRefreshToken())
                            .build();
                    return authResponse;
                }).orElseThrow(() -> new
                        RefreshTokenNotFound(refreshTokenRequest.getRefreshToken()));
    }

}
