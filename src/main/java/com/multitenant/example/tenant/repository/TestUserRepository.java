package com.multitenant.example.tenant.repository;

import com.multitenant.example.tenant.entity.TestUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestUserRepository extends JpaRepository<TestUser, Long> {

    Optional<TestUser> findByUsernameIgnoreCase(String username);
}
