package com.example.presentation;

import com.example.service.ScriptExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/scripts")
public class ExecuteFileController {

    @Autowired
    private ScriptExecutor scriptService;

    @PostMapping("/execute")
    public ResponseEntity<String> executeScript(@RequestParam("file") MultipartFile file) {
        scriptService.executeFlow(file);
        return ResponseEntity.ok("Script ejecutado correctamente");
    }
}
