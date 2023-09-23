package com.multitenant.example.tenant.service;

import com.multitenant.example.tenant.config.service.DomainService;
import com.multitenant.example.tenant.entity.TestUser;
import com.multitenant.example.tenant.entity.UserToken;
import com.multitenant.example.tenant.exceptions.NotFoundException;
import com.multitenant.example.tenant.exceptions.ValueAlreadyExistsException;
import com.multitenant.example.tenant.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserTokenService implements DomainService {

    @Autowired
    private UserTokenRepository userTokenRepository;

    public UserToken registerToken(String generatedToken, TestUser user) {
        Optional<UserToken> tokenOpt = userTokenRepository.findByToken(generatedToken);
        tokenOpt.ifPresent(userToken -> {
            throw new ValueAlreadyExistsException("Token already exists");
        });

        return userTokenRepository.save(new UserToken(generatedToken, user));
    }

    public void validateToken(String token) {
        userTokenRepository.findByToken(token).orElseThrow(() -> {
            throw new NotFoundException("Token not found");
        });
    }

    public void deleteToken(String tokenToRemove) {
        Optional<UserToken> tokenOpt = userTokenRepository.findByToken(tokenToRemove);
        tokenOpt.orElseThrow(() -> {
            throw new NotFoundException("Token not found");
        });

        userTokenRepository.delete(tokenOpt.get());
    }

    public void deleteTokens(TestUser user) {
        userTokenRepository.deleteByUserId(user.getId());
    }

}