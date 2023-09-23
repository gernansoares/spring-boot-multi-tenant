package com.multitenant.example.tenant.config;

import com.multitenant.example.tenant.entity.Tenant;
import com.multitenant.example.tenant.entity.TenantConnection;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
@Component
@Slf4j
public class DataSourceFactory {

    private final String POSTGRES_DRIVER = "org.postgresql.Driver";
    private final String CONNECTION_POOL_NAME = "%s-connection-pool";
    private final Long CONNECTION_TIMEOUT = 10000L;
    private final Long IDLE_TIMEOUT = 200000L;
    private final Integer MIN_IDLE = 3;
    private final Integer POOL_SIZE = 50;

    public DataSource create(Tenant tenant) {
        log.info("Creating datasource for {}", tenant.getConnection().getDatabase());

        HikariDataSource ds = new HikariDataSource();
        TenantConnection connectionInfo = tenant.getConnection();

        ds.setUsername(connectionInfo.getUsername());
        ds.setPassword(connectionInfo.getPassword());
        ds.setJdbcUrl(connectionInfo.getUrl());
        ds.setJdbcUrl(connectionInfo.getUrl());
        ds.setPoolName(String.format(CONNECTION_POOL_NAME, connectionInfo.getDatabase()));
        ds.setDriverClassName(POSTGRES_DRIVER);
        ds.setConnectionTimeout(CONNECTION_TIMEOUT);
        ds.setIdleTimeout(IDLE_TIMEOUT);
        ds.setMinimumIdle(MIN_IDLE);
        ds.setMaximumPoolSize(POOL_SIZE);

        return ds;
    }

}
