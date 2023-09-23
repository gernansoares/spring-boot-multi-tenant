package com.multitenant.example.master.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class TenantConnection {

    @Id
    @GeneratedValue
    private Long id;

    @Size(max = 256)
    @Column(unique = true)
    @NotBlank
    private String database;

    @Size(max = 256)
    @Column(unique = true)
    @NotBlank
    private String url;

    @Size(max = 256)
    @NotBlank
    private String username;

    @Size(max = 256)
    @NotBlank
    private String password;

    @OneToOne(mappedBy = "connection")
    private Tenant tenant;

}
