package com.dailybrief.controller;

import com.dailybrief.config.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/automation")
public class AutomationController {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/trigger")
    public ResponseEntity<?> triggerAutomation(@RequestHeader("Authorization") String authorizationHeader) {
        // Extrai o token do cabeçalho Authorization
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Cabeçalho de autorização inválido");
        }

        String token = authorizationHeader.substring(7); // Remove "Bearer "
        
        // Valida o token
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).body("Token inválido ou expirado");
        }

        // Obtém o usuário autenticado (email) do token
        String username = tokenProvider.getEmailFromToken(token);
        if (username == null) {
            return ResponseEntity.status(401).body("Usuário não encontrado no token");
        }

        // Verifica se o usuário está no contexto de segurança (opcional, já que o JwtAuthenticationFilter já faz isso)
        String authenticatedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!username.equals(authenticatedUser)) {
            return ResponseEntity.status(403).body("Usuário não autorizado");
        }

        // Simula o acionamento da automação
        // Aqui você pode adicionar a lógica para disparar a automação, se necessário.
        // No nosso caso, o FastAPI já faz a automação, então o Spring Boot apenas confirma o acionamento.
        Map<String, String> response = new HashMap<>();
        response.put("message", "Automação acionada com sucesso pelo usuário: " + username);
        response.put("status", "SUCCESS");

        return ResponseEntity.ok(response);
    }
}
