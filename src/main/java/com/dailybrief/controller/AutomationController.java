package com.dailybrief.controller;

import com.dailybrief.dto.AutomationDTO;
import com.dailybrief.dto.TrendingTopicSuggestionDTO;
import com.dailybrief.dto.AutomationResponseDTO;
import com.dailybrief.dto.MaterialProcessResponseDTO;
import com.dailybrief.dto.GenerateContentManualRequestDTO;
import com.dailybrief.dto.SubmitFinalPostRequestDTO;

import com.dailybrief.service.AutomationService;
import com.dailybrief.service.MaterialProcessService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional; // Importação adicionada

@RestController
@RequestMapping("/api/automation")
@SecurityRequirement(name = "bearerAuth")
public class AutomationController {

    private final AutomationService automationService;
    private final MaterialProcessService materialProcessService;

    public AutomationController(
            AutomationService automationService,
            MaterialProcessService materialProcessService) {
        this.automationService = automationService;
        this.materialProcessService = materialProcessService;
    }

    /**
     * Inicia a coleta de material bruto para um tema.
     * Aciona a aplicação Python para coletar material bruto com base em um AutomationRequest existente
     * e cria/atualiza um registro de MaterialProcess no PostgreSQL.
     *
     * @param id O ID do AutomationRequest (do PostgreSQL) que contém o tema e formato.
     * @param jwt O objeto JWT do usuário autenticado para extrair o userId e o token.
     * @return Uma resposta contendo o taskId gerado e o status inicial da tarefa.
     */
    @Operation(summary = "Iniciar a coleta de material bruto para um tema",
               description = "Aciona a aplicação Python para coletar material bruto com base em um AutomationRequest existente e cria/atualiza um registro de MaterialProcess no PostgreSQL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coleta de material iniciada com sucesso. Retorna o taskId para acompanhamento."),
            @ApiResponse(responseCode = "404", description = "AutomationRequest não encontrado para o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno ao iniciar a coleta ou comunicação com a API Python.")
    })
    @PostMapping("/trigger-automation/{id}")
    public ResponseEntity<MaterialProcessResponseDTO> triggerAutomation(
            @Parameter(description = "ID do AutomationRequest a ser processado.") @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        
        String userId = Objects.requireNonNull(jwt.getSubject(), "User ID não pode ser nulo no JWT.");
        String jwtToken = Objects.requireNonNull(jwt.getTokenValue(), "Token JWT não pode ser nulo.");

        AutomationResponseDTO automationRequestDto = automationService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "AutomationRequest com ID " + id + " não encontrado."));

        MaterialProcessResponseDTO responseDto = materialProcessService.initiateAutomationRequest(userId, automationRequestDto, jwtToken);
        
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Consulta o status e os detalhes de uma tarefa de processamento.
     * O frontend fará polling neste endpoint.
     *
     * @param taskId O ID da tarefa gerado pelo Python.
     * @param jwt O objeto JWT do usuário autenticado.
     * @return Os detalhes completos da tarefa, incluindo material bruto ou conteúdo gerado.
     */
    @Operation(summary = "Consultar status e detalhes de uma tarefa de processamento",
               description = "Permite ao frontend verificar o progresso e obter o material bruto ou o conteúdo gerado de uma tarefa específica, buscando dados da aplicação Python e atualizando o registro local.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalhes da tarefa recuperados com sucesso."),
            @ApiResponse(responseCode = "404", description = "Tarefa de processamento não encontrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno ao consultar a API Python ou processar a resposta.")
    })
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<MaterialProcessResponseDTO> getTaskDetails(
            @Parameter(description = "ID da tarefa de processamento.") @PathVariable String taskId,
            @AuthenticationPrincipal Jwt jwt) {
        
        String userId = Objects.requireNonNull(jwt.getSubject(), "User ID não pode ser nulo no JWT.");
        MaterialProcessResponseDTO response = materialProcessService.getMaterialProcessDetails(userId, taskId);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todas as tarefas de processamento de material, com opção de filtrar por status.
     * Busca os registros diretamente do PostgreSQL do Spring Boot.
     *
     * @param status Opcional: O status das tarefas a serem filtradas (por exemplo, "PENDING", "GENERATED").
     * @return Uma lista de DTOs de MaterialProcess.
     */
    @Operation(summary = "Listar todas as tarefas de processamento de material",
               description = "Retorna uma lista de todas as tarefas de automação (MaterialProcess), opcionalmente filtrando por status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tarefas recuperada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno ao listar as tarefas.")
    })
    @GetMapping("/tasks") // Endpoint agora aceita um RequestParam para status
    public ResponseEntity<List<MaterialProcessResponseDTO>> listAllTasks(
            @Parameter(description = "Status das tarefas a serem filtradas (ex: 'RAW_COLLECTED', 'GENERATED').") 
            @RequestParam Optional<String> status) {
        
        List<MaterialProcessResponseDTO> tasks = materialProcessService.listMaterialProcesses(status);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Aciona a geração manual de conteúdo com o Gemini.
     * O frontend envia o material bruto (possivelmente editado) para o Python.
     *
     * @param taskId O ID da tarefa de processamento.
     * @param request DTO contendo o material bruto editado, tema e tipo de conteúdo.
     * @param jwt O objeto JWT do usuário autenticado.
     * @return O DTO do MaterialProcess com o status atualizado para 'GENERATING'.
     */
    @Operation(summary = "Acionar geração manual de conteúdo",
               description = "Envia o material bruto (possivelmente editado) para a aplicação Python para iniciar a geração de conteúdo com o Gemini.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Geração de conteúdo iniciada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Tarefa de processamento não encontrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno ao acionar a API Python ou processar a resposta.")
    })
    @PostMapping("/tasks/{taskId}/generate-content")
    public ResponseEntity<MaterialProcessResponseDTO> triggerManualContentGeneration(
            @Parameter(description = "ID da tarefa de processamento.") @PathVariable String taskId,
            @RequestBody GenerateContentManualRequestDTO request,
            @AuthenticationPrincipal Jwt jwt) {

        String userId = Objects.requireNonNull(jwt.getSubject(), "User ID não pode ser nulo no JWT.");
        MaterialProcessResponseDTO response = materialProcessService.triggerManualContentGeneration(userId, taskId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Submete o post final aprovado pelo usuário.
     * Envia o conteúdo final para a API Python, que o publica no Spring Boot principal e limpa os registros temporários.
     *
     * @param taskId O ID da tarefa de processamento (para referência e deleção).
     * @param request DTO com os dados completos do post final.
     * @param jwt O objeto JWT do usuário autenticado.
     * @return Uma resposta de sucesso após a submissão.
     */
    @Operation(summary = "Submeter post final para publicação",
               description = "Envia o conteúdo do post aprovado pelo usuário para a aplicação Python, que o publica no sistema principal e limpa os dados temporários.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post final submetido com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno ao submeter o post final ou comunicação com a API Python.")
    })
    @PostMapping("/tasks/{taskId}/submit-final")
    public ResponseEntity<Void> submitFinalPost(
            @Parameter(description = "ID da tarefa de processamento.") @PathVariable String taskId,
            @RequestBody SubmitFinalPostRequestDTO request,
            @AuthenticationPrincipal Jwt jwt) {

        String userId = Objects.requireNonNull(jwt.getSubject(), "User ID não pode ser nulo no JWT.");
        materialProcessService.submitFinalPost(userId, taskId, request);
        return ResponseEntity.ok().build();
    }

    /**
     * Salva uma solicitação de automação e chama um serviço externo.
     * NOTE: Este endpoint agora é para salvar a AutomationRequest, o trigger para o Python
     * é feito via /trigger-automation/{id}.
     *
     * @param dto DTO com os dados da automação.
     * @param jwtToken Token JWT para autenticação.
     * @return Resposta do serviço externo.
     */
    @Operation(summary = "Salvar uma solicitação de automação (legado)", description = "Este serviço salva uma solicitação de automação no banco de dados. O trigger para o Python agora é feito via '/trigger-automation/{id}'.")
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
