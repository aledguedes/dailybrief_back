package com.dailybrief.controller;

import com.dailybrief.dto.MaterialProcessResponseDTO;
import com.dailybrief.dto.GenerateContentManualRequestDTO;
import com.dailybrief.dto.SubmitFinalPostRequestDTO;
import com.dailybrief.dto.AutomationResponseDTO;
import com.dailybrief.dto.ImageGenerationRequestDTO;
import com.dailybrief.dto.ImageGenerationResponseDTO;
import com.dailybrief.service.MaterialProcessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Optional;
import java.util.Objects;

@RestController
@RequestMapping("/api/material-process")
public class MaterialProcessController {

    private final MaterialProcessService materialProcessService;

    public MaterialProcessController(MaterialProcessService materialProcessService) {
        this.materialProcessService = materialProcessService;
    }

    /**
     * Endpoint para iniciar uma requisição de automação.
     * 
     * @param jwt                  O JWT do usuário autenticado para obter o ID do
     *                             usuário.
     * @param automationRequestDto O DTO contendo os detalhes da requisição de
     *                             automação.
     * @return ResponseEntity com o DTO do MaterialProcess inicializado.
     */
    @PostMapping("/initiate-automation")
    public ResponseEntity<MaterialProcessResponseDTO> initiateAutomation(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody AutomationResponseDTO automationRequestDto) {
        String userId = Objects.requireNonNull(jwt.getSubject(), "User ID não pode ser nulo no JWT.");
        String jwtToken = Objects.requireNonNull(jwt.getTokenValue(), "Token JWT não pode ser nulo.");
        MaterialProcessResponseDTO response = materialProcessService.initiateAutomationRequest(userId,
                automationRequestDto, jwtToken);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para obter os detalhes de um MaterialProcess específico.
     * 
     * @param jwt    O JWT do usuário autenticado.
     * @param taskId O ID da tarefa do MaterialProcess.
     * @return ResponseEntity com o DTO do MaterialProcess completo.
     */
    @GetMapping("/{taskId}")
    public ResponseEntity<MaterialProcessResponseDTO> getMaterialProcessDetails(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String taskId) {
        String userId = Objects.requireNonNull(jwt.getSubject(), "User ID não pode ser nulo no JWT.");
        MaterialProcessResponseDTO response = materialProcessService.getMaterialProcessDetails(userId, taskId);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para acionar a geração manual de conteúdo.
     * 
     * @param jwt           O JWT do usuário autenticado.
     * @param taskId        O ID da tarefa do MaterialProcess.
     * @param manualRequest O DTO completo com todos os parâmetros para a geração
     *                      Gemini.
     * @return ResponseEntity com o DTO do MaterialProcess atualizado.
     */
    @PostMapping("/{taskId}/generate-manual")
    public ResponseEntity<MaterialProcessResponseDTO> triggerManualContentGeneration(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String taskId,
            @RequestBody GenerateContentManualRequestDTO manualRequest) {
        String userId = Objects.requireNonNull(jwt.getSubject(), "User ID não pode ser nulo no JWT.");
        MaterialProcessResponseDTO response = materialProcessService.triggerManualContentGeneration(userId, taskId,
                manualRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para submeter o post final.
     * 
     * @param jwt           O JWT do usuário autenticado.
     * @param taskId        O ID da tarefa do MaterialProcess.
     * @param submitRequest O DTO com os dados do post final.
     * @return ResponseEntity indicando sucesso.
     */
    @PostMapping("/{taskId}/submit-final-post")
    public ResponseEntity<Void> submitFinalPost(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String taskId,
            @RequestBody SubmitFinalPostRequestDTO submitRequest) {
        String userId = Objects.requireNonNull(jwt.getSubject(), "User ID não pode ser nulo no JWT.");
        materialProcessService.submitFinalPost(userId, taskId, submitRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para listar todos os MaterialProcess, opcionalmente filtrando por
     * status.
     * 
     * @param status Opcional: O status das tarefas a serem filtradas.
     * @return ResponseEntity com uma lista de DTOs de MaterialProcess.
     */
    @GetMapping
    public ResponseEntity<List<MaterialProcessResponseDTO>> listMaterialProcesses(
            @RequestParam Optional<String> status) {
        List<MaterialProcessResponseDTO> response = materialProcessService.listMaterialProcesses(status);
        return ResponseEntity.ok(response);
    }

    /**
     * NOVO ENDPOINT: Gera uma imagem com base em um prompt.
     * 
     * @param jwt         O JWT do usuário autenticado para obter o token para a API
     *                    Python.
     * @param requestBody O DTO contendo o prompt da imagem.
     * @return ResponseEntity com o DTO contendo a imagem gerada em Base64.
     */
    @PostMapping("/generate-image")
    public ResponseEntity<ImageGenerationResponseDTO> generateImage(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody ImageGenerationRequestDTO requestBody) {
        String jwtToken = Objects.requireNonNull(jwt.getTokenValue(), "Token JWT não pode ser nulo.");
        ImageGenerationResponseDTO response = materialProcessService.generateImage(requestBody.imagePrompt(), jwtToken);
        return ResponseEntity.ok(response);
    }
}
