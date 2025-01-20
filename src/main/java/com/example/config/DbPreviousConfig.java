package com.example.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.repository.previous", // Ajusta el paquete de tus repositorios
        entityManagerFactoryRef = "dbPreviousEntityManagerFactory",
        transactionManagerRef = "dbPreviousTransactionManager"
)
public class DbPreviousConfig {

    @Primary
    @Bean(name = "dbPreviousDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db-previous")
    public DataSource dbPreviousDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dbPreviousEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean dbPreviousEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dbPreviousDataSource());
        factoryBean.setPackagesToScan("com.example.entity"); // Ajusta el paquete de tus entidades
        factoryBean.setJpaPropertyMap(Map.of(
                "hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl",
                "hibernate.hbm2ddl.auto", "none",
                "hibernate.dialect", "org.hibernate.dialect.SQLServerDialect" // Especifica el dialecto
        ));
        // Especifica el proveedor de persistencia (Hibernate)
        factoryBean.setPersistenceProvider(new org.hibernate.jpa.HibernatePersistenceProvider());
        return factoryBean;
    }

    @Bean(name = "dbPreviousTransactionManager")
    @Primary
    public PlatformTransactionManager dbPreviousTransactionManager() {
        return new JpaTransactionManager(dbPreviousEntityManagerFactory().getObject());
    }

    @Bean(name = "dbPreviousEntityManager")
    @Primary
    public EntityManager dbPreviousEntityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }
}
