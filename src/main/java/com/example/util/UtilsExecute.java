package com.example.util;

import com.example.exception.InvalidScriptException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UtilsExecute {

    public static String validateAndReadFile(MultipartFile file) throws InvalidScriptException {
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

    public static void pauseWithException(long millis) throws InvalidScriptException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
            throw new InvalidScriptException("Error durante la pausa de " + millis + " milisegundos.", e);
        }
    }
}
