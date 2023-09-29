package com.multitenant.example.tenant.service;

import com.multitenant.example.tenant.config.service.AutoRollbackService;
import com.multitenant.example.tenant.entity.DemoUser;
import com.multitenant.example.tenant.entity.UserToken;
import com.multitenant.example.tenant.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserTokenService implements AutoRollbackService {

    @Autowired
    private UserTokenRepository userTokenRepository;

    public UserToken registerToken(String generatedToken, DemoUser user) {
        Optional<UserToken> tokenOpt = userTokenRepository.findByToken(generatedToken);
        tokenOpt.ifPresent(userToken -> new IllegalArgumentException("Token already exists"));

        return userTokenRepository.save(new UserToken(generatedToken, user));
    }

    public void validateToken(String token) {
        userTokenRepository.findByToken(token).orElseThrow(() -> new IllegalArgumentException("Token not found"));
    }

    public void deleteToken(String tokenToRemove) {
        Optional<UserToken> tokenOpt = userTokenRepository.findByToken(tokenToRemove);
        tokenOpt.orElseThrow(() -> new IllegalArgumentException("Token not found"));

        userTokenRepository.delete(tokenOpt.get());
    }

    public void deleteTokens(DemoUser user) {
        userTokenRepository.deleteByUserId(user.getId());
    }

}
