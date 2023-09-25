package com.multitenant.example.master.repository;

import com.multitenant.example.master.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByConnection_DatabaseIgnoreCase(String database);
    Optional<Tenant> findByDomainIgnoreCase(String domain);

}
