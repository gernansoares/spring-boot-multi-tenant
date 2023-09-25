package com.multitenant.example.tenant.controller;

import com.multitenant.example.master.service.TenantService;
import com.multitenant.example.tenant.dto.NewUserDTO;
import com.multitenant.example.tenant.entity.TestUser;
import com.multitenant.example.tenant.service.TestUserService;
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
@RequestMapping("/newuser")
@Slf4j
public class NewTestUserController {

    @Autowired
    private TestUserService testUserService;

    @Autowired
    private TenantService tenantService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Creates a new user and returns its username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Password/confirmation does not match | Username already in use"),
    })
    public ResponseEntity<String> create(@RequestBody @Valid NewUserDTO newUserDto) {
        String tenantId = tenantService.resolveTenantIdByDomain(newUserDto.getDomain());
        TenantContext.setCurrentTenant(tenantId);
        TestUser user = TestUser.of(newUserDto);

        log.info("Adding user {}", user.getUsername());

        user = testUserService.create(user, newUserDto.getPasswordConfirm());

        log.info("User {} added as {} successfully", newUserDto.getUsername(), user.getUsername());

        return ResponseEntity.ok(user.getUsername());
    }

}
