package com.example.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityManagerConfig {

    @PersistenceUnit(name = "previous") // Debe coincidir con el nombre del PersistenceUnit configurado
    private EntityManagerFactory previousEntityManagerFactory;

    @PersistenceUnit(name = "production")
    private EntityManagerFactory productionEntityManagerFactory;

    @Bean(name = "previousEntityManager")
    public EntityManager previousEntityManager() {
        return previousEntityManagerFactory.createEntityManager();
    }

    @Bean(name = "productionEntityManager")
    public EntityManager productionEntityManager() {
        return productionEntityManagerFactory.createEntityManager();
    }
}
