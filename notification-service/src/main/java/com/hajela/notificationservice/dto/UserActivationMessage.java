package com.hajela.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivationMessage implements Serializable {
    private String email;
    private String role;
//    private LocalDateTime createdAt;
    private String activationToken;
}
