package com.multitenant.example.domain.config.security;

import com.multitenant.example.domain.config.security.jwt.JwtUtil;
import com.multitenant.example.domain.service.UserTokenService;
import com.multitenant.example.tenant.config.TenantContext;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Authentication process, validates token integrity and it's presence in database
 */
@Component
@Slf4j
public class AuthenticationManagerImpl implements AuthenticationManager {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserTokenService userTokenService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        try {
            String token = authentication.getCredentials().toString();
            jwtUtil.validateToken(token);

            String tenantId = jwtUtil.getAudienceFromToken(token);
            Claims claims = jwtUtil.getAllClaimsFromToken(token);
            List<String> rolesMap = claims.get("role", List.class);

            TenantContext.setCurrentTenant(tenantId);
            userTokenService.validateToken(token);

            return new UsernamePasswordAuthenticationToken(
                    jwtUtil.getUsernameFromToken(token), token,
                    rolesMap.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        } catch (Exception e) {
            return null;
        }

    }


}