package com.example.service;


import com.example.entity.LogError;
import com.example.exception.InvalidScriptException;
import com.example.repository.previous.PreviousLogRepository;
import com.example.repository.production.ProductionLogRepository;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScriptExecutorService {

    private Logger logger = LoggerFactory.getLogger(ScriptExecutorService.class);

    @Autowired
    @Qualifier("previousEntityManager")
    private EntityManager previousEntityManager;

    @Autowired
    private PreviousLogRepository previousRepository;

    @Autowired
    private ProductionLogRepository productionRepository;

    @Autowired
    @Qualifier("productionEntityManager")
    private EntityManager productionEntityManager;

    public void executeFlow(MultipartFile file){
        try {
            previousExecute(file);
        } catch (InvalidScriptException e){
            e.printStackTrace();
        }

    }

    @Transactional(rollbackFor = { InvalidScriptException.class })
    public void previousExecute(MultipartFile file) throws InvalidScriptException {
        String script = validateAndReadFile(file);

        // Obtener la marca de tiempo actual antes de ejecutar el script
        LocalDateTime executionTime = LocalDateTime.now();

        // Ejecutar el script
        previousEntityManager.createNativeQuery(script).executeUpdate();

        // Pausa de 10 segundos
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InvalidScriptException("Error durante la pausa de 10 segundos.", e);
        }

        // Verificar log_errores usando JPA con condición WHERE
        List<LogError> errores = previousRepository.findByInitDateAfter(executionTime);
        logger.info("El select muestra : {}",errores);
        if (!errores.isEmpty()) {
            throw new InvalidScriptException("Se encontraron errores en la tabla log_errores.");
        }
    }

    public void productionExecute(MultipartFile file){

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
