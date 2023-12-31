package com.multitenant.example.tenant.entity;

import com.multitenant.example.tenant.dto.NewUserDTO;
import com.multitenant.example.tenant.util.UserUtils;
import com.multitenant.example.tenant.validation.NameValidation;
import com.multitenant.example.tenant.validation.UsernameValidation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DemoUser {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @NameValidation
    private String name;

    @UsernameValidation
    @Column(unique = true)
    private String username;

    @NotBlank
    private String password;

    private boolean enabled;

    public static DemoUser of(NewUserDTO newUserDTO) {
        DemoUser user = new DemoUser();
        user.setName(newUserDTO.getName());
        user.setUsername(UserUtils.prepareUsername(newUserDTO.getUsername()));
        user.setPassword(UserUtils.encodePassword(newUserDTO.getPassword()));
        user.setEnabled(true);
        return user;
    }

}
