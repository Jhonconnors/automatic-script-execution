package com.example.repository.previous;

import com.example.entity.LogError;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PreviousLogRepository extends JpaRepository<LogError, Long> {

    List<LogError> findByInitDateAfter(LocalDateTime initDate);
}
