package com.multitenant.example.tenant.dto;

import com.multitenant.example.tenant.validation.NameValidation;
import com.multitenant.example.tenant.validation.PasswordValidation;
import com.multitenant.example.tenant.validation.UsernameValidation;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewUserDTO {

    @NameValidation
    private String name;

    @UsernameValidation
    private String username;

    @PasswordValidation
    private String password;

    @PasswordValidation
    private String passwordConfirm;

    @NotBlank
    private String domain;


}
