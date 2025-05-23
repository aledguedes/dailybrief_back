// LogController.java
package com.dailybrief.controller;

import com.dailybrief.dto.LogRequestDTO;
import com.dailybrief.dto.LogResponseDTO;
import com.dailybrief.service.LogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "*")
public class LogController {

    @Autowired
    private LogService logService;

    @PostMapping
    public ResponseEntity<LogResponseDTO> logAction(@Valid @RequestBody LogRequestDTO logRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Extrai 'sub' do token JWT
        LogResponseDTO response = logService.logAction(logRequest, username);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<LogResponseDTO>> getLogs(Pageable pageable) {
        Page<LogResponseDTO> logs = logService.getLogs(pageable);
        return ResponseEntity.ok(logs);
    }
}