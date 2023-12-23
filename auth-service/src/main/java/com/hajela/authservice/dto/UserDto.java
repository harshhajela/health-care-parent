package com.hajela.authservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hajela.authservice.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String role;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;
    private String status;

    public static UserDto from(UserEntity user) {
        return UserDto.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .role(user.getRole().getName().getRoleName())
                .created(user.getCreated().toLocalDateTime())
                .status(user.getStatus())
                .build();
    }
}
