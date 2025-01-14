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
        basePackages = "com.example.repository.previous", // Cambia el paquete según tu proyecto
        entityManagerFactoryRef = "previousEntityManagerFactory",
        transactionManagerRef = "previousTransactionManager"
)
public class PreviousDatabaseConfig {

    @Bean(name = "previousDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db-previous")
    public DataSource previousDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "previousEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean previousEntityManagerFactory(
            @Qualifier("previousDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.entity") // Paquete donde están las entidades
                .persistenceUnit("previous")
                .build();
    }

    @Bean(name = "previousTransactionManager")
    public PlatformTransactionManager previousTransactionManager(
            @Qualifier("previousEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
