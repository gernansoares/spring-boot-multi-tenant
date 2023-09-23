package com.multitenant.example.tenant.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Standardizes the formatting for username and the encoding password for TestUser
 */
@Component
public class UserUtils {

    private static BCryptPasswordEncoder staticEncoder;

    @Autowired
    public void setEncoder(BCryptPasswordEncoder encoder) {
        staticEncoder = encoder;
    }

    public static String prepareUsername(String username) {
        return username.toLowerCase().replaceAll(" ", "");
    }

    public static String encodePassword(String password) {
        return staticEncoder.encode(password);
    }

}
