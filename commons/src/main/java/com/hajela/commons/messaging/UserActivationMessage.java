package com.hajela.commons.messaging;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivationMessage implements Serializable {
    private String email;
    private String role;
    @ToString.Exclude
    private String activationToken;
}
