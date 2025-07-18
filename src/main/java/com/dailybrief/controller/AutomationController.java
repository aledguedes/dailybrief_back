package com.dailybrief.controller;

import com.dailybrief.dto.AutomationDTO;
import com.dailybrief.dto.TrendingTopicSuggestionDTO;
import com.dailybrief.service.AutomationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/automation")
public class AutomationController {

    private final AutomationService automationService;

    @Autowired
    public AutomationController(AutomationService automationService) {
        this.automationService = automationService;
    }

    /**
     * Salva uma solicitação de automação e chama um serviço externo.
     *
     * @param dto DTO com os dados da automação.
     * @param jwtToken Token JWT para autenticação.
     * @return Resposta do serviço externo.
     */
    @Operation(summary = "Salvar uma solicitação de automação", description = "Este serviço salva uma solicitação de automação no banco de dados e chama um serviço externo para processá-la.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitação de automação salva com sucesso."),
        @ApiResponse(responseCode = "400", description = "Erro ao chamar serviço externo ou erro de conexão.")
    })
    @PostMapping("/request")
    public ResponseEntity<String> saveAutomationRequest(
            @Parameter(description = "DTO contendo os dados da automação.") @RequestBody AutomationDTO dto,
            @Parameter(description = "Token JWT de autenticação.") @RequestHeader String jwtToken) {

        String response = automationService.saveAutomationRequest(dto, jwtToken);
        return ResponseEntity.ok(response);
    }

    /**
     * Salva ou atualiza uma lista de sugestões de tópicos em tendência.
     *
     * @param dtos Lista de DTOs com as sugestões de tópicos.
     */
    @Operation(summary = "Salvar sugestões de tópicos", description = "Este serviço salva ou atualiza as sugestões de tópicos em tendência no banco de dados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sugestões de tópicos salvas com sucesso."),
        @ApiResponse(responseCode = "400", description = "Lista de sugestões vazia ou inválida.")
    })
    @PostMapping("/suggestions")
    public ResponseEntity<Void> saveSuggestions(
            @Parameter(description = "Lista de DTOs com as sugestões de tópicos.") @RequestBody List<TrendingTopicSuggestionDTO> dtos) {

        automationService.saveSuggestions(dtos);
        return ResponseEntity.ok().build();
    }

    /**
     * Obtém uma lista de sugestões de tópicos com base no status fornecido.
     *
     * @param status O status das sugestões (por exemplo, "PENDENTE", "APROVADO").
     * @return Lista de sugestões de tópicos.
     */
    @Operation(summary = "Obter sugestões de tópicos por status", description = "Este serviço obtém uma lista de sugestões de tópicos com base no status fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de sugestões recuperada com sucesso."),
        @ApiResponse(responseCode = "400", description = "Status inválido.")
    })
    @GetMapping("/suggestions/status/{status}")
    public ResponseEntity<List<TrendingTopicSuggestionDTO>> getSuggestionsByStatus(
            @Parameter(description = "Status das sugestões (por exemplo, 'PENDENTE', 'APROVADO').") @PathVariable String status) {

        List<TrendingTopicSuggestionDTO> suggestions = automationService.getSuggestionsByStatus(status);
        return ResponseEntity.ok(suggestions);
    }

    /**
     * Atualiza uma sugestão de tópico existente com novos dados.
     *
     * @param id ID da sugestão a ser atualizada.
     * @param dto DTO com os novos dados para a sugestão.
     * @return DTO com a sugestão atualizada.
     */
    @Operation(summary = "Atualizar sugestão de tópico", description = "Este serviço atualiza uma sugestão de tópico existente com novos dados fornecidos no DTO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sugestão de tópico atualizada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Sugestão não encontrada para o ID fornecido.")
    })
    @PutMapping("/suggestions/{id}")
    public ResponseEntity<TrendingTopicSuggestionDTO> updateSuggestion(
            @Parameter(description = "ID da sugestão a ser atualizada.") @PathVariable Long id,
            @Parameter(description = "DTO contendo os novos dados da sugestão.") @RequestBody TrendingTopicSuggestionDTO dto) {

        TrendingTopicSuggestionDTO updatedSuggestion = automationService.updateSuggestion(id, dto);
        return ResponseEntity.ok(updatedSuggestion);
    }
}
