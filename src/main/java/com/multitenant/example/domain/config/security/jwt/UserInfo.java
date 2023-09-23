package com.multitenant.example.domain.config.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.multitenant.example.domain.entity.TestUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Defines fields por authentication process
 */
@Getter
@AllArgsConstructor
@ToString
public class UserInfo {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private Boolean enabled;

    private List<String> roles;

    public String getUsername() {
        return username;
    }

    public boolean isAccountNonExpired() {
        return false;
    }

    public boolean isAccountNonLocked() {
        return false;
    }

    public boolean isCredentialsNonExpired() {
        return false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

}