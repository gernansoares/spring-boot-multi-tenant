package com.multitenant.example.master.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Tenant {

    @Id
    @GeneratedValue
    private Long id;

    @Size(max = 256)
    @NotBlank
    private String name;

    @Size(max = 256)
    @NotBlank
    @Column(unique = true)
    private String domain;

    @NotBlank
    private Boolean status;

    @NotBlank
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private TenantConnection connection;

}
