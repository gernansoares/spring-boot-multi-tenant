package com.multitenant.example.tenant.dto;

import com.multitenant.example.tenant.entity.DemoUser;
import lombok.Data;

@Data
public class ListUserDTO {

    private String name;

    private String username;

    public static ListUserDTO of(DemoUser user) {
        ListUserDTO dto = new ListUserDTO();
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        return dto;
    }

}
