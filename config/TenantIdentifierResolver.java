package com.multitenant.example.tenant.config;

import org.apache.logging.log4j.util.Strings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    public static final String DEFAULT_TENANT_ID = "demo";

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContext.getCurrentTenant();
        return Strings.isNotBlank(tenantId) ? tenantId : DEFAULT_TENANT_ID;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
