package com.multitenant.example.tenant.dto;

import com.multitenant.example.tenant.validation.NameValidation;
import com.multitenant.example.tenant.validation.PasswordValidation;
import com.multitenant.example.tenant.validation.UsernameValidation;
import lombok.Data;

@Data
public class UpdateUserDTO {

    private Long id;

    @NameValidation
    private String name;

    @UsernameValidation
    private String username;

    @PasswordValidation
    private String password;

    @PasswordValidation
    private String passwordConfirm;


}
