package com.dailybrief.controller;

import com.dailybrief.dto.*;
import com.dailybrief.service.AutomationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.UUID;

@RestController
@RequestMapping("/api/automation")
@CrossOrigin(origins = "*")
public class AutomationController {

        private final AutomationService automationService;

        public AutomationController(AutomationService automationService) {
                this.automationService = automationService;
        }

        @Operation(summary = "Inicia a automação de postagem a partir de uma URL", description = "Dispara o processo de extração, geração e postagem de conteúdo baseado em uma URL.")
        @ApiResponses({
                        @ApiResponse(responseCode = "202", description = "Automação iniciada com sucesso. O processamento ocorrerá em segundo plano."),
                        @ApiResponse(responseCode = "400", description = "URL inválida ou ausente no corpo da requisição.")
        })
        @PostMapping("/trigger-by-url")
        @ResponseStatus(HttpStatus.ACCEPTED)
        public Mono<ResponseEntity<String>> triggerByUrl(@Valid @RequestBody AutomationRequestDTO request) {
                return automationService.triggerByUrl(request.url())
                                .thenReturn(new ResponseEntity<>("Processo de automação iniciado em segundo plano.",
                                                HttpStatus.ACCEPTED));
        }

        @Operation(summary = "Consulta o resultado de uma tarefa de automação", description = "Busca o status e o resultado do conteúdo gerado para um ID de tarefa específico.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Resultado da tarefa retornado com sucesso."),
                        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada para o ID fornecido.")
        })
        @GetMapping("/get-task-result/{taskId}")
        public Mono<TaskResultDTO> getTaskResult(@PathVariable String taskId) {
                return automationService.getTaskResult(taskId);
        }

        @Operation(summary = "Inicia a automação de postagem a partir de múltiplas URLs", description = "Dispara o processo de extração, geração e postagem de conteúdo para uma lista de URLs.")
        @ApiResponses({
                        @ApiResponse(responseCode = "202", description = "Automação para múltiplas URLs iniciada. O processamento ocorrerá em segundo plano."),
                        @ApiResponse(responseCode = "400", description = "Lista de URLs vazia ou inválida no corpo da requisição.")
        })
        @PostMapping("/trigger-multiple-urls")
        @ResponseStatus(HttpStatus.ACCEPTED)
        public Mono<ResponseEntity<String>> triggerMultipleUrls(
                        @RequestHeader("Authorization") String authorizationHeader,
                        @Valid @RequestBody AutomationMultipleUrlsRequestDTO request,
                        Authentication authentication) {

                String token = authorizationHeader.replace("Bearer ", "");
                String userId = authentication.getName();

                return automationService.triggerMultipleUrls(
                                request.urls(),
                                token,
                                userId,
                                request.theme(),
                                request.outputFormat())
                                .thenReturn(new ResponseEntity<>(
                                                "Processo de automação para múltiplas URLs iniciado em segundo plano.",
                                                HttpStatus.ACCEPTED));
        }

        @Operation(summary = "Inicia a automação de postagem a partir de um texto", description = "Dispara o processo de extração de tema e geração de conteúdo a partir de um texto fornecido.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Automação iniciada com sucesso. Retorna o ID da nova tarefa."),
                        @ApiResponse(responseCode = "400", description = "Texto inválido ou ausente no corpo da requisição.")
        })
        @PostMapping("/trigger-by-text")
        public Mono<TriggerResponseDTO> triggerByText(@Valid @RequestBody TriggerByTextRequestDTO request) {
                return automationService.triggerByText(request.text());
        }

        @Operation(summary = "Lista todas as tarefas de automação iniciadas", description = "Retorna uma lista de todas as tarefas de automação, com seus IDs e status.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso.")
        })
        @GetMapping("/tasks")
        public Flux<TriggerTaskResponseDTO> getAllTriggerTasks() {
                return automationService.getAllTriggerTasks();
        }

        @Operation(summary = "Busca uma tarefa de automação por ID", description = "Retorna os detalhes de uma tarefa específica, incluindo seu status e IDs.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Tarefa encontrada e retornada com sucesso."),
                        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada.")
        })
        @GetMapping("/tasks/{id}")
        public Mono<ResponseEntity<TriggerTaskResponseDTO>> getTriggerTaskById(@PathVariable UUID id) {
                return automationService.getTriggerTaskById(id)
                                .map(optional -> optional.map(ResponseEntity::ok)
                                                .orElseGet(() -> ResponseEntity.notFound().build()));
        }

        @Operation(summary = "Lista todo o material bruto associado a um usuário", description = "Busca e retorna todos os materiais brutos (URLs, textos) associados ao usuário autenticado.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Lista de materiais retornada com sucesso."),
                        @ApiResponse(responseCode = "401", description = "Não autorizado.")
        })
        @GetMapping("/user/materials/{userId}")
        public Mono<RawMaterialsListResponseDTO> listUserMaterials(@PathVariable String userId) {
                return automationService.listUserMaterials(userId);
        }

        @Operation(summary = "Lista todos os materiais brutos de uma tarefa", description = "Retorna todos os materiais brutos que fazem parte de uma única tarefa de automação.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Materiais retornados com sucesso."),
                        @ApiResponse(responseCode = "404", description = "Tarefa ou materiais não encontrados.")
        })
        @GetMapping("/raw-materials-by-task/{taskId}")
        public Mono<RawMaterialsListResponseDTO> listRawMaterialsByTask(@PathVariable String taskId) {
                return automationService.listRawMaterialsByTask(taskId);
        }

        @Operation(summary = "Extrai conteúdo bruto de uma ou mais URLs", description = "Coleta o texto e os dados de uma lista de URLs, criando um material bruto para uso posterior na geração de conteúdo.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Processo de extração iniciado com sucesso. Retorna o ID da nova tarefa."),
                        @ApiResponse(responseCode = "400", description = "Lista de URLs vazia ou inválida no corpo da requisição.")
        })
        @PostMapping("/extract-from-urls")
        public Mono<TriggerResponseDTO> extractFromUrls(@Valid @RequestBody AutomationMultipleUrlsRequestDTO request) {
                return automationService.extractFromUrls(request.urls());
        }

        @Operation(summary = "Gera conteúdo a partir de materiais brutos selecionados manualmente", description = "Utiliza uma lista de IDs de materiais brutos para gerar o conteúdo final.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Geração de conteúdo iniciada com sucesso. Retorna o ID da nova tarefa."),
                        @ApiResponse(responseCode = "400", description = "Lista de IDs de materiais brutos inválida ou ausente.")
        })
        @PostMapping("/generate-content-manual")
        public Mono<TriggerResponseDTO> generateContentManual(
                        @Valid @RequestBody GenerateContentManualRequestDTO request) {
                return automationService.generateContentManual(request.rawMaterialIds());
        }

        @Operation(summary = "Gera conteúdo para uma tarefa específica", description = "Inicia o processo de geração de conteúdo para todos os materiais brutos associados a uma tarefa existente.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Geração de conteúdo para a tarefa iniciada com sucesso."),
                        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada para o ID fornecido.")
        })
        @PostMapping("/generate/{taskId}")
        public Mono<TriggerResponseDTO> generateContent(@PathVariable String taskId) {
                return automationService.generateContent(taskId);
        }

        @Operation(summary = "Busca o conteúdo bruto de um material por ID", description = "Retorna o conteúdo de texto completo de um material bruto específico (artigo, texto, etc.).")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Conteúdo do material retornado com sucesso."),
                        @ApiResponse(responseCode = "404", description = "Material bruto não encontrado para o ID fornecido.")
        })
        @GetMapping("/raw-material/{rawMaterialId}")
        public Mono<RawContentResponseDTO> getRawMaterialContent(@PathVariable String rawMaterialId) {
                return automationService.getRawMaterialContent(rawMaterialId);
        }

        @Operation(summary = "Submete o conteúdo final gerado para postagem", description = "Pega o conteúdo gerado de uma tarefa e o publica na plataforma de destino.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Conteúdo final submetido com sucesso."),
                        @ApiResponse(responseCode = "400", description = "ID da tarefa inválido ou ausente.")
        })
        @PostMapping("/submit-final-post")
        @ResponseStatus(HttpStatus.OK)
        public Mono<ResponseEntity<String>> submitFinalPost(@Valid @RequestBody SubmitFinalPostRequestDTO request) {
                return automationService.submitFinalPost(request.taskId())
                                .thenReturn(new ResponseEntity<>("Conteúdo final submetido para postagem com sucesso.",
                                                HttpStatus.OK));
        }

        @Operation(summary = "Deleta o material bruto de uma tarefa", description = "Remove todos os materiais brutos associados a um ID de tarefa específico.")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Material deletado com sucesso."),
                        @ApiResponse(responseCode = "404", description = "Tarefa ou material não encontrado.")
        })
        @DeleteMapping("/delete-material/{taskId}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public Mono<Void> deleteMaterial(@PathVariable String taskId) {
                return automationService.deleteMaterial(taskId);
        }

        @Operation(summary = "Gera uma imagem para a tarefa", description = "Cria uma imagem a partir do conteúdo gerado para uma tarefa específica e retorna a URL da imagem.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Imagem gerada com sucesso. Retorna a URL."),
                        @ApiResponse(responseCode = "404", description = "Tarefa ou conteúdo não encontrado para o ID fornecido.")
        })
        @PostMapping("/generate-image")
        @ResponseStatus(HttpStatus.OK)
        public Mono<ImageUrlResponseDTO> generateImage(@Valid @RequestBody SubmitFinalPostRequestDTO request) {
                return automationService.generateImage(request.taskId());
        }
}