package com.hajela.authservice.config;

import java.lang.annotation.*;

/**
 * Custom annotation to define role-based access control
 * Usage: @RoleBasedSecurity({"ADMIN", "PROVIDER"})
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RoleBasedSecurity {
    String[] value() default {};
    boolean requireAuth() default true;
}