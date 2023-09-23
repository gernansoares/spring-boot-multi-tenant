package com.multitenant.example.domain.dto;

import com.multitenant.example.domain.validation.NameValidation;
import com.multitenant.example.domain.validation.PasswordValidation;
import com.multitenant.example.domain.validation.UsernameValidation;
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
    private String tenantId;


}
