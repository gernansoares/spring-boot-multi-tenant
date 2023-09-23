package com.multitenant.example.tenant.repository;

import com.multitenant.example.tenant.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    Optional<UserToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM UserToken u WHERE u.user.id = :userId")
    void deleteByUserId(Long userId);

}
