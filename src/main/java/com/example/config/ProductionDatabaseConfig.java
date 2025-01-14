package com.example.config;


import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.example.repository.production", // Cambia el paquete según tu proyecto
        entityManagerFactoryRef = "productionEntityManagerFactory",
        transactionManagerRef = "productionTransactionManager"
)
public class ProductionDatabaseConfig {

    @Bean(name = "productionDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db-production")
    public DataSource productionDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "productionEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean productionEntityManagerFactory(
            @Qualifier("productionDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.entity") // Mismo paquete de entidades
                .persistenceUnit("production")
                .build();
    }

    @Bean(name = "productionTransactionManager")
    public PlatformTransactionManager productionTransactionManager(
            @Qualifier("productionEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
