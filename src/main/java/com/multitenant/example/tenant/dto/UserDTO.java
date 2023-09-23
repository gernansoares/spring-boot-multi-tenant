package com.multitenant.example.tenant.dto;

import com.multitenant.example.tenant.entity.TestUser;
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
