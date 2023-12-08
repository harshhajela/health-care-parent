package com.hajela.authservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "role") @Data @Builder @AllArgsConstructor @NoArgsConstructor
public class RoleEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private Role name;

    public static RoleEntity from(Role role) {
        return RoleEntity.builder().name(role).build();
    }
}
