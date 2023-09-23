package com.multitenant.example.domain.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Starts the authentication process when authorization header is found and is a Bearer token
 */
@Component
@Slf4j
public class SecurityContextRepository implements org.springframework.security.web.context.SecurityContextRepository {

    @Autowired
    private AuthenticationManagerImpl authenticationManagerImpl;

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        String authorization = requestResponseHolder.getRequest().getHeader(HttpHeaders.AUTHORIZATION);

        if (!Objects.isNull(authorization) && authorization.startsWith(SecurityContants.BEARER_TOKEN_PREFIX)) {
            String authToken = authorization.replaceAll(SecurityContants.BEARER_TOKEN_PREFIX, "");
            Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
            return new SecurityContextImpl(this.authenticationManagerImpl.authenticate(auth));
        }

        return null;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return false;
    }
}