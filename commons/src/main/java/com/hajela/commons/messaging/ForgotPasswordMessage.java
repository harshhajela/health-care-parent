package com.hajela.commons.messaging;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordMessage implements Serializable {
    private String email;
    @ToString.Exclude
    private String token;
}
