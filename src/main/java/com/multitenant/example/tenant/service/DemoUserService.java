package com.multitenant.example.tenant.service;

import com.multitenant.example.master.config.TenantContext;
import com.multitenant.example.tenant.config.security.jwt.JwtUtil;
import com.multitenant.example.tenant.config.security.jwt.UserInfo;
import com.multitenant.example.tenant.config.service.AutoRollbackService;
import com.multitenant.example.tenant.dto.AuthRequestDTO;
import com.multitenant.example.tenant.dto.UpdateUserDTO;
import com.multitenant.example.tenant.entity.DemoUser;
import com.multitenant.example.tenant.repository.DemoUserRepository;
import com.multitenant.example.tenant.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DemoUserService implements AutoRollbackService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private DemoUserRepository demoUserRepository;

    @Autowired
    private UserTokenService userTokenService;

    private void validatePasswordAndConfirmationMatch(String hashedPassword, String passwordConfirm) {
        if (!BCrypt.checkpw(passwordConfirm, hashedPassword)) {
            throw new IllegalArgumentException("Password/confirmation does not match");
        }
    }

    private void validateExistingUsername(Long idUser, String username) {
        findByUsername(username).ifPresent(dbUser -> {
            if (!Objects.equals(dbUser.getId(), idUser)) {
                throw new IllegalArgumentException("Username already exists");
            }
        });
    }

    public DemoUser create(DemoUser user, String passwordConfirm) {
        validateExistingUsername(user.getId(), user.getUsername());
        validatePasswordAndConfirmationMatch(user.getPassword(), passwordConfirm);
        return demoUserRepository.save(user);
    }

    public DemoUser update(UpdateUserDTO userDTO) {
        String username = UserUtils.prepareUsername(userDTO.getUsername());
        String password = UserUtils.encodePassword(userDTO.getPassword());

        DemoUser user = demoUserRepository.findById(userDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        validateExistingUsername(user.getId(), username);
        validatePasswordAndConfirmationMatch(password, userDTO.getPasswordConfirm());

        user.setName(userDTO.getName());
        user.setUsername(username);
        user.setPassword(password);
        return demoUserRepository.save(user);
    }

    public void delete(Long id) {
        demoUserRepository.findById(id).ifPresentOrElse(dbUser -> {
            userTokenService.deleteTokens(dbUser);
            demoUserRepository.delete(dbUser);
        }, () -> new IllegalArgumentException("User not found"));
    }

    public Optional<DemoUser> findByUsername(String username) {
        return demoUserRepository.findByUsernameIgnoreCase(username);
    }

    public Optional<DemoUser> findById(Long id) {
        return demoUserRepository.findById(id);
    }

    public List<DemoUser> findAll() {
        return demoUserRepository.findAll();
    }

    public String login(AuthRequestDTO request) {
        Optional<DemoUser> userOpt = findByUsername(UserUtils.prepareUsername(request.getUsername()));

        DemoUser user = userOpt.orElseThrow(() -> new IllegalArgumentException("User not found"));

        validatePasswordAndConfirmationMatch(user.getPassword(), request.getPassword());

        UserInfo userInfo = new UserInfo(user.getUsername(), user.getPassword(), user.isEnabled(), List.of("USER"));
        String token = jwtUtil.generateToken(userInfo, TenantContext.getCurrentTenant());

        userTokenService.registerToken(token, user);

        return token;
    }

}
