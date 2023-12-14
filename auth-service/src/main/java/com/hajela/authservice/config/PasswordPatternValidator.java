package com.hajela.authservice.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;

@RequiredArgsConstructor
public class PasswordPatternValidator implements ConstraintValidator<PasswordPattern, String> {

    private final Environment environment;
    private String pattern;

    @Override
    public void initialize(PasswordPattern constraintAnnotation) {
        this.pattern = environment.getProperty("registration.validation.password-pattern");
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(pattern);
    }
}
