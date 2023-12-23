package com.hajela.authservice.messaging.dto;

import com.hajela.authservice.entities.ForgotPasswordEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordMessage {
    private String email;
    private String token;

    public static ForgotPasswordMessage from(ForgotPasswordEntity forgotPasswordEntity) {
        return ForgotPasswordMessage.builder()
                .email(forgotPasswordEntity.getUser().getEmail())
                .token(forgotPasswordEntity.getToken())
                .build();
    }
}
