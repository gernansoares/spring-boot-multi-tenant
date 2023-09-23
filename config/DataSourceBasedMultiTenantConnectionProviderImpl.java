package com.multitenant.example.tenant.config;

import com.multitenant.example.tenant.entity.Tenant;
import com.multitenant.example.tenant.exceptions.TenantNotFoundException;
import com.multitenant.example.tenant.repository.TenantRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.*;

@Configuration
@Slf4j
public class DataSourceBasedMultiTenantConnectionProviderImpl
        extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private final String DEFAULT_TENANT_ID = "tenant";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private DataSourceFactory dataSourceFactory;

    private Map<String, DataSource> dataSources = new HashMap<>();

    @Override
    protected DataSource selectAnyDataSource() {
        log.info("Selecting any datasource");
        loadTenants();
        return dataSources.get(DEFAULT_TENANT_ID);
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        String tenantId = TenantContext.getCurrentTenant();
        log.info("Getting datasource for {}", tenantId);

        if (!dataSources.containsKey(tenantId)) {
            Optional<Tenant> tenantOpt = tenantRepository.findByConnection_Database(tenantId);

            tenantOpt.ifPresentOrElse(tenant -> addTenant(tenant),
                    () -> {
                        throw new TenantNotFoundException(tenantId);
                    });
        }

        return this.dataSources.get(tenantId);
    }

    private void loadTenants() {
        if (dataSources.isEmpty()) {
            log.info("Getting all datasources available");

            tenantRepository.findAll().forEach(tenant -> {
                addTenant(tenant);
            });
        }
    }

    private void addTenant(Tenant tenant) {
        Objects.requireNonNull(tenant);
        log.info("Adding tenant {}", tenant.getConnection().getDatabase());
        dataSources.put(tenant.getConnection().getDatabase(), dataSourceFactory.create(tenant));
    }

}
