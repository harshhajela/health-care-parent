package com.hajela.authservice.messaging.dto;

import com.hajela.authservice.entities.UserActivationEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivationMessage {

    private String email;
    private String role;
    private String activationToken;

    public static UserActivationMessage from(UserActivationEntity userActivationEntity) {
        return UserActivationMessage.builder()
                .email(userActivationEntity.getUser().getEmail())
                .role(userActivationEntity.getUser().getRole().getName().getRoleName())
                .activationToken(userActivationEntity.getToken())
                .build();
    }
}
