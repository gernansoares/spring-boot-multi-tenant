package com.multitenant.example.tenant.config;

import org.apache.logging.log4j.util.Strings;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Resolves the tenant of the active thread through the TenantContext
 */
@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    public static final String DEFAULT_TENANT_ID = "tenant";

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
