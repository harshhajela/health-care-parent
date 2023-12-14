package com.hajela.authservice.entities;


import com.hajela.authservice.dto.RegistrationRequest;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_tbl")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @ToString.Exclude
    private String password;
    private String status;
    @Builder.Default
    private Timestamp created = new Timestamp(System.currentTimeMillis());

    @ManyToOne(fetch = FetchType.EAGER)
    private RoleEntity role;

    public static UserEntity from(RegistrationRequest userDto) {
        return UserEntity.builder()
                .email(userDto.getEmail())
                .created(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
