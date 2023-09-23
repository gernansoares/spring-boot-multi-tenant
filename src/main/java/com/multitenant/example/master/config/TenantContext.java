package com.multitenant.example.master.config;

/**
 * Holds the tenant ID for each thread
 */
public class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = ThreadLocal.withInitial(() -> TenantIdentifierResolver.DEFAULT_TENANT_ID);

    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    public static void setCurrentTenant(String tenant) {
        CURRENT_TENANT.set(tenant);
    }

}
