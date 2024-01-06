package com.hajela.authservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hajela.authservice.config.PasswordPattern;
import com.hajela.authservice.entities.Role;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @PasswordPattern
    private String password;

    @NotBlank(message = "Confirm Password cannot be blank")
    private String confirmPassword;

    @NotNull(message = "Role cannot be null")
    private Role role;

    @JsonIgnore
    @AssertTrue(message = "Password and Confirm Password must match")
    public boolean isPasswordsMatch() {
        return password != null && password.equals(confirmPassword);
    }
}
