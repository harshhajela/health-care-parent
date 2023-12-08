package com.hajela.authservice.dto;

import com.hajela.authservice.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    private String email;
    private String password;
    private String confirmPassword;
    private Role role;
}
