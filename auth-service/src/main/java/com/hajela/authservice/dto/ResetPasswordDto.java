package com.hajela.authservice.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDto {
    private String token;

    @ToString.Exclude
    private String newPassword;
    @ToString.Exclude
    private String confirmNewPassword;
}
