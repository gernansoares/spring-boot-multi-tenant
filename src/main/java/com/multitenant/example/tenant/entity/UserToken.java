package com.multitenant.example.tenant.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserToken {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Column(unique = true, columnDefinition = "TEXT")
    private String token;

    @ManyToOne
    private TestUser user;

    public UserToken(String token, TestUser user) {
        this.token = token;
        this.user = user;
    }

}
