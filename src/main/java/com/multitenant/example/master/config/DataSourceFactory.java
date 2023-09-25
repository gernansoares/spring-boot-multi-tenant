package com.multitenant.example.master.config;

import com.multitenant.example.master.entity.Tenant;
import com.multitenant.example.master.entity.TenantConnection;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Creates new datasources for each tenant
 */
@Component
@Slf4j
public class DataSourceFactory {

    private final String CONNECTION_POOL_NAME = "%s-connection-pool";

    @Value("${multitenant.per-schema}")
    private boolean schema;

    public DataSource create(Tenant tenant) {
        HikariDataSource ds = new HikariDataSource();
        TenantConnection connectionInfo = tenant.getConnection();

        ds.setUsername(connectionInfo.getUsername());
        ds.setPassword(connectionInfo.getPassword());
        ds.setJdbcUrl(connectionInfo.getUrl());
        ds.setPoolName(String.format(CONNECTION_POOL_NAME, connectionInfo.getDatabase()));
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setConnectionTimeout(10000L);
        ds.setIdleTimeout(200000L);
        ds.setMinimumIdle(3);
        ds.setMaximumPoolSize(50);
        if (schema) {
            ds.setSchema(tenant.getConnection().getDatabase());
        }

        return ds;
    }

}
