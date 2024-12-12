package com.example.service;

import com.example.entity.LogError;
import com.example.exception.InvalidScriptException;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScriptService {

    @Autowired
    private EntityManager entityManager;

    @Transactional(rollbackFor = { InvalidScriptException.class })
    public void executeScript(MultipartFile file) throws InvalidScriptException {
        String script = validateAndReadFile(file);

        // Obtener la marca de tiempo actual antes de ejecutar el script
        LocalDateTime executionTime = LocalDateTime.now();

        // Ejecutar el script
        entityManager.createNativeQuery(script).executeUpdate();

        // Pausa de 10 segundos
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InvalidScriptException("Error durante la pausa de 10 segundos.", e);
        }

        // Verificar log_errores usando JPA con condición WHERE
        List<LogError> errores = entityManager.createQuery("SELECT e FROM LogError e WHERE e.initDate > :executionTime")
                .setParameter("executionTime", executionTime)
                .getResultList();
        if (!errores.isEmpty()) {
            throw new InvalidScriptException("Se encontraron errores en la tabla log_errores.");
        }
    }

    private String validateAndReadFile(MultipartFile file) throws InvalidScriptException {
        try (InputStream inputStream = file.getInputStream()) {
            String script = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            if (script.contains("DROP") || script.contains("ALTER") ||
                    script.contains("TRUNCATE") ||
                    (script.contains("DELETE") && !script.matches("(?i).*WHERE.*"))) {
                throw new InvalidScriptException("El script contiene operaciones no permitidas.");
            }

            return script;
        } catch (IOException e) {
            throw new InvalidScriptException("Error al leer el archivo.", e);
        }
    }
}
