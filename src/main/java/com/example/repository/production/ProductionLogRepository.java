package com.example.repository.production;

import com.example.entity.LogError;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionLogRepository extends JpaRepository<LogError, Long> {
}
