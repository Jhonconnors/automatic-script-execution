package com.example.repository.production;

import com.example.entity.LogError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogErrorProductionRepository extends JpaRepository<LogError, Long> {
    List<LogError> findByRegisterDateAfter(LocalDateTime date);
}