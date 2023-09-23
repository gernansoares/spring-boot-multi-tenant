package com.multitenant.example.tenant.service;

import com.multitenant.example.tenant.config.security.jwt.JwtUtil;
import com.multitenant.example.tenant.config.security.jwt.UserInfo;
import com.multitenant.example.tenant.config.service.DomainService;
import com.multitenant.example.tenant.dto.AuthRequest;
import com.multitenant.example.tenant.dto.UserUpdateDTO;
import com.multitenant.example.tenant.entity.TestUser;
import com.multitenant.example.tenant.exceptions.*;
import com.multitenant.example.tenant.repository.TestUserRepository;
import com.multitenant.example.tenant.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TestUserService implements DomainService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TestUserRepository testUserRepository;

    @Autowired
    private UserTokenService userTokenService;

    private void validatePasswordAndConfirmationMatch(String hashedPassword, String passwordConfirm) {
        if (!BCrypt.checkpw(passwordConfirm, hashedPassword)) {
            throw new InvalidInfoException("Password/confirmation does not match");
        }
    }

    private void validateExistingUsername(Long idUser, String username) {
        findByUsername(username).ifPresent(dbUser -> {
            if (!Objects.equals(dbUser.getId(), idUser)) {
                throw new ValueAlreadyExistsException("Username already exists");
            }
        });
    }

    public TestUser create(TestUser user, String passwordConfirm) {
        validateExistingUsername(user.getId(), user.getUsername());
        validatePasswordAndConfirmationMatch(user.getPassword(), passwordConfirm);
        return testUserRepository.save(user);
    }

    public TestUser update(UserUpdateDTO userDTO) {
        String username = UserUtils.prepareUsername(userDTO.getUsername());
        String password = UserUtils.encodePassword(userDTO.getPassword());

        TestUser user = testUserRepository.findById(userDTO.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        validateExistingUsername(user.getId(), username);
        validatePasswordAndConfirmationMatch(password, userDTO.getPasswordConfirm());

        user.setName(userDTO.getName());
        user.setUsername(username);
        user.setPassword(password);
        return testUserRepository.save(user);
    }

    public void delete(Long id) {
        testUserRepository.findById(id).ifPresentOrElse(dbUser -> {
            userTokenService.deleteTokens(dbUser);
            testUserRepository.delete(dbUser);
        }, () -> {
            throw new NotFoundException("User not found");
        });
    }

    public Optional<TestUser> findByUsername(String username) {
        return testUserRepository.findByUsernameIgnoreCase(username);
    }

    public Optional<TestUser> findById(Long id) {
        return testUserRepository.findById(id);
    }

    public List<TestUser> findAll() {
        return testUserRepository.findAll();
    }

    public String login(AuthRequest request) {
        Optional<TestUser> userOpt = findByUsername(UserUtils.prepareUsername(request.getUsername()));

        TestUser user = userOpt.orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.isEnabled()) {
            throw new DisabledException();
        }

        validatePasswordAndConfirmationMatch(user.getPassword(), request.getPassword());

        UserInfo userInfo = new UserInfo(user.getUsername(), user.getPassword(), user.isEnabled(), List.of("USER"));
        String token = jwtUtil.generateToken(userInfo, request.getTenantId());

        userTokenService.registerToken(token, user);

        return token;
    }

}
