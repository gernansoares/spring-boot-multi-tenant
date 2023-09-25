package com.multitenant.example.tenant.repository;

import com.multitenant.example.tenant.entity.DemoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DemoUserRepository extends JpaRepository<DemoUser, Long> {

    Optional<DemoUser> findByUsernameIgnoreCase(String username);
}
