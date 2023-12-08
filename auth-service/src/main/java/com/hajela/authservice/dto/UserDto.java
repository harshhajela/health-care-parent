package com.hajela.authservice.dto;

import com.hajela.authservice.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String email;
    private String role;
    private Timestamp created;

    public static UserDto from(UserEntity user) {
        return UserDto.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .role(user.getRole().getName().getRoleName())
                .created(user.getCreated())
                .build();
    }
}
