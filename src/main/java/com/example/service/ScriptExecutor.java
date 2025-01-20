package com.example.service;

import com.example.exception.InvalidScriptException;
import com.example.usecases.UseCaseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public class ScriptExecutor {

    private Logger logger = LoggerFactory.getLogger(ScriptExecutor.class);

    private final List<UseCaseHandler<MultipartFile, Boolean>> useCaseHandlers;

    public ScriptExecutor(List<UseCaseHandler<MultipartFile, Boolean>> useCaseHandlers) {
        this.useCaseHandlers = useCaseHandlers;
    }


    public void executeFlow(MultipartFile file){
        try {
            for (UseCaseHandler<MultipartFile, Boolean> handler : useCaseHandlers) {
                logger.info("Ejecutando handler: {}", handler.getClass().getSimpleName());

                Boolean result = handler.execute(file);

                if (result !=  null && !result) {
                    logger.error("El handler {} falló, generando un error.", handler.getClass().getSimpleName());
                    throw new InvalidScriptException("Se produjo un error durante la ejecución del handler.");
                }
            }
        } catch (InvalidScriptException e){
            logger.error(e.getMessage());
        }

    }

}
