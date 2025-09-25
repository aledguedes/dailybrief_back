package com.dailybrief.controller;

import com.dailybrief.dto.python.*;
import com.dailybrief.service.AutomationService;
import com.dailybrief.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/automation")
@CrossOrigin(origins = "*")
public class AutomationController {

    private final AutomationService automationService;
    private final PostService postService;

    public AutomationController(AutomationService automationService, PostService postService) {
        this.automationService = automationService;
        this.postService = postService;
    }

    @Operation(summary = "Aciona automação para múltiplas URLs")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Automação acionada com sucesso", content = @Content(schema = @Schema(implementation = TriggerResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PostMapping("/multiple-urls")
    public Mono<ResponseEntity<TriggerResponseDTO>> triggerMultipleUrls(
            @RequestBody AutomationTriggerRequestDTO requestDTO,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String authToken = authHeader.replace("Bearer ", "");
        return automationService.triggerMultipleUrls(requestDTO, authToken)
                .map(response -> ResponseEntity.accepted().body(response));
    }

    @Operation(summary = "Aciona automação por texto")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Automação acionada com sucesso", content = @Content(schema = @Schema(implementation = TriggerResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PostMapping("/by-text")
    public Mono<ResponseEntity<TriggerResponseDTO>> triggerByText(
            @RequestBody TriggerByTextRequestDTO requestDTO,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String authToken = authHeader.replace("Bearer ", "");
        return automationService.triggerByText(requestDTO, authToken)
                .map(response -> ResponseEntity.accepted().body(response));
    }

    @Operation(summary = "Aciona automação por URL")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Automação acionada com sucesso", content = @Content(schema = @Schema(implementation = TriggerResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PostMapping("/by-url")
    public Mono<ResponseEntity<TriggerResponseDTO>> triggerByUrl(
            @RequestBody TriggerByUrlRequestDTO requestDTO,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String authToken = authHeader.replace("Bearer ", "");
        return automationService.triggerByUrl(requestDTO, authToken)
                .map(response -> ResponseEntity.accepted().body(response));
    }

    @Operation(summary = "Aciona a automação por ID", description = "Inicia a automação usando um ID pré-existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Automação acionada com sucesso", content = @Content(schema = @Schema(implementation = TriggerResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado, token inválido"),
            @ApiResponse(responseCode = "404", description = "ID não encontrado")
    })
    @GetMapping("/trigger-by-id/{id}")
    public Mono<ResponseEntity<TriggerResponseDTO>> triggerById(@PathVariable String id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String auth_token = authHeader.replace("Bearer ", "");
        return automationService.triggerById(id, auth_token)
                .map(response -> new ResponseEntity<>(response, HttpStatus.ACCEPTED));
    }

    @Operation(summary = "Gera conteúdo manualmente", description = "Gera conteúdo a partir de um request manual, sem automação prévia.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Geração de conteúdo iniciada", content = @Content(schema = @Schema(implementation = GenerateContentResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado, token inválido"),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos")
    })
    @PostMapping("/generate-content-manual")
    public Mono<ResponseEntity<GenerateContentResponseDTO>> generateContentManual(
            @RequestBody ContentGenerationRequestDTO requestDTO,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String auth_token = authHeader.replace("Bearer ", "");
        return automationService.generateContentManual(requestDTO, auth_token)
                .map(response -> new ResponseEntity<>(response, HttpStatus.ACCEPTED));
    }

    @Operation(summary = "Gera conteúdo", description = "Gera conteúdo baseado em materiais brutos já existentes.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Geração de conteúdo iniciada", content = @Content(schema = @Schema(implementation = GenerateContentResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado, token inválido"),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos")
    })
    @PostMapping("/generate-content")
    public Mono<ResponseEntity<GenerateContentResponseDTO>> generateContent(
            @RequestBody ContentGenerationRequestDTO requestDTO,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String auth_token = authHeader.replace("Bearer ", "");
        return automationService.generateContent(requestDTO, auth_token)
                .map(response -> new ResponseEntity<>(response, HttpStatus.ACCEPTED));
    }

    @Operation(summary = "Gera imagem", description = "Gera uma imagem a partir de um prompt.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Geração de imagem iniciada", content = @Content(schema = @Schema(implementation = GenerateContentResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado, token inválido"),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos")
    })
    @PostMapping("/generate-image")
    public Mono<ResponseEntity<GenerateContentResponseDTO>> generateImage(
            @RequestBody ContentGenerationRequestDTO requestDTO,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String auth_token = authHeader.replace("Bearer ", "");
        return automationService.generateImage(requestDTO, auth_token)
                .map(response -> new ResponseEntity<>(response, HttpStatus.ACCEPTED));
    }

    @Operation(summary = "Gera conteúdo por ID da tarefa", description = "Gera conteúdo a partir de uma tarefa existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Geração de conteúdo iniciada", content = @Content(schema = @Schema(implementation = GenerateContentResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado, token inválido"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @PostMapping("/generate/{taskId}")
    public Mono<ResponseEntity<GenerateContentResponseDTO>> generateByTaskId(@PathVariable String taskId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String auth_token = authHeader.replace("Bearer ", "");
        return automationService.generateByTaskId(taskId, auth_token)
                .map(response -> new ResponseEntity<>(response, HttpStatus.ACCEPTED));
    }

    @Operation(summary = "Verifica o status da tarefa", description = "Verifica o status atual de uma tarefa de automação usando seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status da tarefa retornado com sucesso", content = @Content(schema = @Schema(implementation = GenerateContentResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado, token inválido"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @GetMapping("/status/{taskId}")
    public Mono<ResponseEntity<GenerateContentResponseDTO>> getAutomationStatus(@PathVariable String taskId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String auth_token = authHeader.replace("Bearer ", "");
        return automationService.getTaskResult(taskId, auth_token)
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Lista materiais brutos do usuário", description = "Recupera uma lista de todos os materiais brutos para o usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de materiais retornada com sucesso", content = @Content(schema = @Schema(implementation = RawMaterialsListResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado, token inválido"),
            @ApiResponse(responseCode = "404", description = "Materiais não encontrados")
    })
    @GetMapping("/materials")
    public Mono<ResponseEntity<RawMaterialsListResponseDTO>> listUserMaterials(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String auth_token = authHeader.replace("Bearer ", "");
        return automationService.listUserMaterials(auth_token)
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Obtém material bruto por ID", description = "Recupera o conteúdo de um material bruto específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Material bruto retornado com sucesso", content = @Content(schema = @Schema(implementation = RawMaterialResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado, token inválido"),
            @ApiResponse(responseCode = "404", description = "Material bruto não encontrado")
    })
    @GetMapping("/materials/{rawMaterialId}")
    public Mono<ResponseEntity<RawMaterialResponseDTO>> getRawMaterial(@PathVariable String rawMaterialId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String auth_token = authHeader.replace("Bearer ", "");
        return automationService.getRawMaterial(rawMaterialId, auth_token)
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Obtém materiais por ID da tarefa", description = "Recupera todos os materiais brutos associados a uma tarefa específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Materiais brutos retornados com sucesso", content = @Content(schema = @Schema(implementation = RawMaterialsListResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado, token inválido"),
            @ApiResponse(responseCode = "404", description = "Materiais não encontrados para a tarefa")
    })
    @GetMapping("/materials/by-task/{taskId}")
    public Mono<ResponseEntity<RawMaterialsListResponseDTO>> getRawMaterialsByTask(@PathVariable String taskId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String auth_token = authHeader.replace("Bearer ", "");
        return automationService.getRawMaterialsByTask(taskId, auth_token)
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Submete o post final", description = "Endpoint para o serviço de automação submeter o post finalizado. **Esta rota é consumida pelo serviço Python**.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Post recebido e salvo com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar o post")
    })
    @PostMapping("/submit-final-post")
    public Mono<ResponseEntity<Void>> submitFinalPost(@RequestBody FinalPostSubmissionRequestDTO requestDTO) {
        return postService.saveGeneratedPost(requestDTO)
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
    }
}