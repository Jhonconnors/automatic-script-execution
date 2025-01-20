package com.example.usecases;

import com.example.entity.LogError;
import com.example.exception.InvalidScriptException;
import com.example.repository.previous.LogErrorPreviousRepository;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.util.UtilsExecute.pauseWithException;
import static com.example.util.UtilsExecute.validateAndReadFile;

@Service
@Order(1)
public class PreviousUseCase implements UseCaseHandler<MultipartFile, Boolean>{

    private Logger logger = LoggerFactory.getLogger(PreviousUseCase.class);

    private final LogErrorPreviousRepository logErrorPreviousRepository;

    private final EntityManager dbPreviousEntityManager;

    public PreviousUseCase(
            LogErrorPreviousRepository logErrorPreviousRepository,
            @Qualifier("dbPreviousEntityManagerFactory") EntityManager dbPreviousEntityManager
    ) {
        this.logErrorPreviousRepository = logErrorPreviousRepository;
        this.dbPreviousEntityManager = dbPreviousEntityManager;
    }


    @Override
    @Transactional(rollbackFor = { InvalidScriptException.class })
    public Boolean execute(MultipartFile request) throws InvalidScriptException {

        String script = validateAndReadFile(request);

        // Obtener la marca de tiempo actual antes de ejecutar el script
        LocalDateTime executionTime = LocalDateTime.now();

        // Ejecutar el script
        dbPreviousEntityManager.createNativeQuery(script).executeUpdate();

        // Pausa de 15 segundos
        pauseWithException(1500);

        // Verificar log_errores usando JPA con condición WHERE
        List<LogError> errores = logErrorPreviousRepository.findByRegisterDateAfter(executionTime);
        logger.info("El select muestra : {}",errores);
        if (!errores.isEmpty()) {
            throw new InvalidScriptException("Se encontraron errores en la tabla log_errores.");
        }
        return true;
    }
}
