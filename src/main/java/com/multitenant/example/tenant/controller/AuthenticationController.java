package com.multitenant.example.tenant.controller;

import com.multitenant.example.tenant.dto.AuthRequest;
import com.multitenant.example.tenant.dto.AuthResponse;
import com.multitenant.example.tenant.service.TestUserService;
import com.multitenant.example.tenant.service.UserTokenService;
import com.multitenant.example.master.config.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

    @Autowired
    private TestUserService testUserService;

    @Autowired
    private UserTokenService userTokenService;

    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Provides user authentication by checking username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Information is correct, a new token is generated"),
            @ApiResponse(responseCode = "401", description = "Incorrect login information"),
    })
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest ar) {
        String tenantId = ar.getTenantId();
        TenantContext.setCurrentTenant(tenantId);

        log.info("Logging user with username {}", ar.getUsername());

        String token = testUserService.login(ar);

        log.info("User {} logged in", ar.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

}