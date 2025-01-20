package com.example.entity;
import jakarta.persistence.*;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "LogError")
@ToString
public class LogError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdLogError")
    private Long idLogError;

    @Column(name = "JsonValue", columnDefinition = "nvarchar(max)")
    private String jsonValue;

    @Column(name = "IndExecute")
    private Boolean indExecute;

    @Column(name = "IndProcess")
    private Boolean indProcess;

    @Column(name = "RegisterDate")
    private LocalDateTime registerDate;

    // Getters y setters
    public Long getIdLogError() {
        return idLogError;
    }

    public void setIdLogError(Long idLogError) {
        this.idLogError = idLogError;
    }

    public String getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    public Boolean getIndExecute() {
        return indExecute;
    }

    public void setIndExecute(Boolean indExecute) {
        this.indExecute = indExecute;
    }

    public Boolean getIndProcess() {
        return indProcess;
    }

    public void setIndProcess(Boolean indProcess) {
        this.indProcess = indProcess;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }


}
