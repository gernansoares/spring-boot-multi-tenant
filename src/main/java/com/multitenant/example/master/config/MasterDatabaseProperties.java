package com.multitenant.example.master.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Connection properties for master database
 */
@Configuration
@ConfigurationProperties("multitenant.master.datasource")
@Data
public class MasterDatabaseProperties {

    private String url;

    private String username;

    private String password;

    private String driverClassName;

    private long connectionTimeout;

    private int maxPoolSize;

    private long idleTimeout;

    private int minIdle;

    private String poolName;

}
