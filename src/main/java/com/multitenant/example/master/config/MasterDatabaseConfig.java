package com.multitenant.example.master.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Defines configuration for the master (default) database which should be used to manage tenants or the app
 * Every bellow com.multitenant.example.tenant will be automatically scanned to be part of the master scope
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.multitenant.example.master.*"})
@EnableJpaRepositories(basePackages = {"com.multitenant.example.master.*"},
        entityManagerFactoryRef = "masterEntityManagerFactory",
        transactionManagerRef = "masterTransactionManager")
public class MasterDatabaseConfig {

    @Autowired
    private MasterDatabaseProperties masterDatabaseProperties;

    @Bean(name = "masterDataSource")
    public DataSource getMasterDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername(masterDatabaseProperties.getUsername());
        hikariDataSource.setPassword(masterDatabaseProperties.getPassword());
        hikariDataSource.setJdbcUrl(masterDatabaseProperties.getUrl());
        hikariDataSource.setDriverClassName(masterDatabaseProperties.getDriverClassName());
        hikariDataSource.setMaximumPoolSize(masterDatabaseProperties.getMaxPoolSize());
        hikariDataSource.setMinimumIdle(masterDatabaseProperties.getMinIdle());
        hikariDataSource.setConnectionTimeout(masterDatabaseProperties.getConnectionTimeout());
        hikariDataSource.setIdleTimeout(masterDatabaseProperties.getIdleTimeout());
        hikariDataSource.setPoolName("multitenant-connection-pool");
        return hikariDataSource;
    }

    @Primary
    @Bean(name = "masterEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        factory.setDataSource(getMasterDataSource());
        factory.setPackagesToScan("com.multitenant.example.master");
        factory.setPersistenceUnitName("multitenant-persistence-unit");
        factory.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.put(Environment.SHOW_SQL, false);
        properties.put(Environment.FORMAT_SQL, true);
        properties.put(Environment.HBM2DDL_AUTO, "update");
        properties.put(Environment.ENABLE_LAZY_LOAD_NO_TRANS, "true");
        factory.setJpaProperties(properties);

        return factory;
    }

    @Bean(name = "masterTransactionManager")
    public JpaTransactionManager masterTransactionManager(@Qualifier("masterEntityManagerFactory") EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

}
