package com.multitenant.example.domain.entity;

import com.multitenant.example.domain.dto.NewUserDTO;
import com.multitenant.example.domain.dto.UserUpdateDTO;
import com.multitenant.example.domain.util.UserUtils;
import com.multitenant.example.domain.validation.NameValidation;
import com.multitenant.example.domain.validation.UsernameValidation;
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
public class TestUser {

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

    public static TestUser of(NewUserDTO newUserDTO) {
        TestUser user = new TestUser();
        user.setName(newUserDTO.getName());
        user.setUsername(UserUtils.prepareUsername(newUserDTO.getUsername()));
        user.setPassword(UserUtils.encodePassword(newUserDTO.getPassword()));
        user.setEnabled(true);
        return user;
    }

}
