package com.multitenant.example.domain.dto;

import com.multitenant.example.domain.entity.TestUser;
import com.multitenant.example.domain.validation.NameValidation;
import com.multitenant.example.domain.validation.PasswordValidation;
import com.multitenant.example.domain.validation.UsernameValidation;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {

    private String name;

    private String username;

    public static UserDTO of(TestUser user) {
        UserDTO dto = new UserDTO();
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        return dto;
    }

}
