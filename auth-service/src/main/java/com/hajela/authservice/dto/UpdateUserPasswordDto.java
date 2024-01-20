package com.hajela.authservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hajela.authservice.config.PasswordPattern;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserPasswordDto {

    @ToString.Exclude
    @NotBlank(message = "Current password cannot be blank")
    private String currentPassword;

    @PasswordPattern
    @ToString.Exclude
    @NotBlank(message = "New password cannot be blank")
    private String newPassword;

    @ToString.Exclude
    @NotBlank(message = "Confirm new password cannot be blank")
    private String confirmNewPassword;

    @JsonIgnore
    @AssertTrue(message = "New password and Confirm new password must match")
    public boolean isPasswordsMatch() {
        return newPassword != null && newPassword.equals(confirmNewPassword);
    }

}
