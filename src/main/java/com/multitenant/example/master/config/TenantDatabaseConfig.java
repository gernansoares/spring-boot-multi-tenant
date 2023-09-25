package com.multitenant.example.master.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines configuration for tenants databases
 * Every bellow com.multitenant.example.domain will be automatically scanned to be part of the tenants scope
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.multitenant.example.tenant.*"})
@EnableJpaRepositories(basePackages = {"com.multitenant.example.tenant.*"},
        entityManagerFactoryRef = "tenantEntityManagerFactory",
        transactionManagerRef = "tenantTransactionManager")
@Slf4j
public class TenantDatabaseConfig {

    @Autowired
    private MasterDatabaseProperties masterDatabaseProperties;

    @Bean(name = "tenantJpaVendorAdapter")
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean(name = "tenantTransactionManager")
    public JpaTransactionManager transactionManager(@Qualifier("tenantEntityManagerFactory")
                                                    EntityManagerFactory tenantEntityManager) {
        return new JpaTransactionManager(tenantEntityManager);
    }

    @Bean(name = "multiTenantConnectionProvider")
    @ConditionalOnBean(name = "masterEntityManagerFactory")
    public MultiTenantConnectionProvider multiTenantConnectionProvider() {
        return new MultiTenantConnectionProviderImpl();
    }

    @Bean(name = "currentTenantIdentifierResolver")
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        return new TenantIdentifierResolver();
    }

    @Bean(name = "tenantEntityManagerFactory")
    @ConditionalOnBean(name = "multiTenantConnectionProvider")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("multiTenantConnectionProvider")
            MultiTenantConnectionProvider connectionProvider,
            @Qualifier("currentTenantIdentifierResolver")
            CurrentTenantIdentifierResolver tenantResolver) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        factory.setPackagesToScan("com.multitenant.example.tenant");
        factory.setPersistenceUnitName("tenantdb-persistence-unit");
        factory.setJpaVendorAdapter(jpaVendorAdapter());
        factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);

        Map<String, Object> properties = new HashMap<>();
        properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
        properties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
        properties.put(Environment.SHOW_SQL, false);
        properties.put(Environment.FORMAT_SQL, true);
        properties.put(Environment.HBM2DDL_AUTO, "update");
        properties.put(Environment.ENABLE_LAZY_LOAD_NO_TRANS, "true");
        factory.setJpaPropertyMap(properties);

        return factory;
    }
}
