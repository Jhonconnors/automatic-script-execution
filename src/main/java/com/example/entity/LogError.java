package com.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_errores")
public class LogError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "casuse", nullable = false)
    private String casuse;

    @Column(name = "init_date", nullable = false)
    private LocalDateTime initDate;

    @Column(name = "work_area", nullable = false)
    private String workArea;

    // Constructores
    public LogError() {}

    public LogError(String casuse, LocalDateTime initDate, String workArea) {
        this.casuse = casuse;
        this.initDate = initDate;
        this.workArea = workArea;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCasuse() {
        return casuse;
    }

    public void setCasuse(String casuse) {
        this.casuse = casuse;
    }

    public LocalDateTime getInitDate() {
        return initDate;
    }

    public void setInitDate(LocalDateTime initDate) {
        this.initDate = initDate;
    }

    public String getWorkArea() {
        return workArea;
    }

    public void setWorkArea(String workArea) {
        this.workArea = workArea;
    }
}
