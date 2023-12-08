package com.hajela.authservice.entities;

import lombok.Getter;

@Getter
public enum Role {
    CUSTOMER("Customer"),
    PROVIDER("Provider"),
    ADMIN("Admin");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public static Role fromRole(String roleName) {
        for (Role role : Role.values()) {
            if (role.getRoleName().equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No role found for the given string: " + roleName);
    }
}

