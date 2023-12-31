package com.multitenant.example.tenant.controller;

import com.multitenant.example.tenant.dto.ListUserDTO;
import com.multitenant.example.tenant.dto.UpdateUserDTO;
import com.multitenant.example.tenant.entity.DemoUser;
import com.multitenant.example.tenant.service.DemoUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/testuser")
@Slf4j
public class TestUserController {

    @Autowired
    private DemoUserService demoUserService;

    @PutMapping("{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Updates a user and returns its username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "400", description = "User not found | Password/confirmation does not match | Username already in use"),
    })
    public ResponseEntity<String> update(@PathVariable String userId,
                                         @RequestBody @Valid UpdateUserDTO userDTO) {
        log.info("Updating user {}", userDTO.getUsername());

        userDTO.setId(Long.parseLong(userId));
        DemoUser user = demoUserService.update(userDTO);

        log.info("User {} updated as {} successfully", userDTO.getUsername(), user.getUsername());

        return ResponseEntity.ok(user.getUsername());
    }

    @DeleteMapping("{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Updates a user and returns its username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "400", description = "User not found"),
    })
    public ResponseEntity delete(@PathVariable String userId) {
        log.info("Deleting user with ID {}", userId);

        demoUserService.delete(Long.parseLong(userId));

        log.info("User with ID {} deleted successfully", userId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Updates a user and returns its username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "400", description = "User not found"),
    })
    public ResponseEntity<ListUserDTO> findById(@PathVariable String userId) {
        log.info("Getting user with ID {}", userId);

        DemoUser user = demoUserService.findById(Long.parseLong(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        log.info("User with ID {} returned successfully", userId);

        return ResponseEntity.ok(ListUserDTO.of(user));
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Return all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users list"),
    })
    public ResponseEntity<List<ListUserDTO>> findAll() {
        log.info("Getting all users");

        return ResponseEntity.ok(demoUserService.findAll().stream()
                .map(user -> ListUserDTO.of(user)).collect(Collectors.toList()));
    }

}
