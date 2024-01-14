package com.hajela.authservice.services;

import com.hajela.authservice.dto.*;

public interface AuthService {

    public AuthResponse login(AuthRequest authRequest);

    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    public AuthResponse activateAccount(ActivateAccountDto activateAccountDto);

    public void forgotPassword(ForgotPasswordDto forgotPasswordDto);

    public void resetUserPassword(ResetPasswordDto resetPasswordDto);

    public void logout(String authorizationHeader);
}
