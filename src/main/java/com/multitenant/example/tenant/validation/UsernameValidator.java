package com.multitenant.example.tenant.validation;

import com.multitenant.example.tenant.util.UserUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class UsernameValidator implements ConstraintValidator<UsernameValidation, String> {

    public boolean isValid(String username, ConstraintValidatorContext cxt) {
        username = UserUtils.prepareUsername(username);
        return Objects.nonNull(username) && username.length() >= 5 && username.length() <= 20;
    }

}