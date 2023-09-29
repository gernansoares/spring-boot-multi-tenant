package com.multitenant.example.master.service;

import com.multitenant.example.master.entity.Tenant;
import com.multitenant.example.master.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    public String resolveTenantIdByDomain(String domain) {
        Tenant tenant = tenantRepository.findByDomainIgnoreCase(domain)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));
        return tenant.getConnection().getDatabase();
    }

}
