package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.repository.production", // Ajusta el paquete de tus repositorios
        entityManagerFactoryRef = "dbProductionEntityManagerFactory",
        transactionManagerRef = "dbProductionTransactionManager"
)
public class DbProductionConfig {

    @Bean(name = "dbProductionDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db-production")
    public DataSource dbProductionDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dbProductionEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean dbProductionEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dbProductionDataSource());
        factoryBean.setPackagesToScan("com.example.entity"); // Ajusta el paquete de tus entidades
        factoryBean.setJpaPropertyMap(Map.of(
                "hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl",
                "hibernate.hbm2ddl.auto", "none",
                "hibernate.dialect", "org.hibernate.dialect.SQLServerDialect" // Especifica el dialecto
        ));
        // Especifica el proveedor de persistencia (Hibernate)
        factoryBean.setPersistenceUnitName("dbProduction");
        factoryBean.setPersistenceProvider(new org.hibernate.jpa.HibernatePersistenceProvider());
        return factoryBean;
    }

    @Bean(name = "dbProductionTransactionManager")
    public PlatformTransactionManager dbProductionTransactionManager() {
        return new JpaTransactionManager(dbProductionEntityManagerFactory().getObject());
    }

}
