package com.dailybrief.service.impl;

import com.dailybrief.dto.MaterialProcessInitialDTO;
import com.dailybrief.dto.MaterialProcessResponseDTO;
import com.dailybrief.dto.PythonMaterialResponseDTO;
import com.dailybrief.dto.PythonTriggerResponseDTO;
import com.dailybrief.dto.SubmitFinalPostRequestDTO;
import com.dailybrief.dto.AutomationResponseDTO;
import com.dailybrief.dto.GenerateContentManualRequestDTO;
import com.dailybrief.dto.ImageGenerationRequestDTO;
import com.dailybrief.dto.ImageGenerationResponseDTO;
import com.dailybrief.mapper.MaterialProcessMapper;
import com.dailybrief.model.MaterialProcess;
import com.dailybrief.repository.MaterialProcessRepository;
import com.dailybrief.service.MaterialProcessService;
import com.dailybrief.config.ApplicationConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaterialProcessServiceImpl implements MaterialProcessService {

        private final MaterialProcessRepository materialProcessRepository;
        private final MaterialProcessMapper materialProcessMapper;
        private final WebClient webClient;
        private final ObjectMapper objectMapper;

        public MaterialProcessServiceImpl(
                        MaterialProcessRepository materialProcessRepository,
                        MaterialProcessMapper materialProcessMapper,
                        WebClient.Builder webClientBuilder,
                        ApplicationConfig applicationConfig) {
                this.materialProcessRepository = materialProcessRepository;
                this.materialProcessMapper = materialProcessMapper;
                this.webClient = webClientBuilder.baseUrl(applicationConfig.getPythonApiUrl()).build();
                this.objectMapper = new ObjectMapper();
        }

        @Override
        public MaterialProcessResponseDTO initiateAutomationRequest(String userId,
                        AutomationResponseDTO automationRequestDto, String jwtToken) {
                // Salva o tema e o outputFormat do AutomationRequestDto no MaterialProcess
                MaterialProcessInitialDTO initialDtoForDb = new MaterialProcessInitialDTO(
                                userId,
                                null, // taskId será preenchido após a chamada ao Python
                                automationRequestDto.theme(), // Usando o tema real
                                automationRequestDto.outputFormat(), // Usando o outputFormat real como contentType
                                                                     // inicial
                                "PENDING_COLLECTION");
                MaterialProcess materialProcess = materialProcessMapper.toEntity(initialDtoForDb);
                materialProcess = materialProcessRepository.save(materialProcess);

                // Constrói a URI para a API Python, incluindo theme e content_type como query
                // parameters
                String pythonTriggerUri = UriComponentsBuilder.fromPath("/trigger-by-id/{id}")
                                .queryParam("theme", automationRequestDto.theme()) // Passa o tema
                                .queryParam("content_type", automationRequestDto.outputFormat()) // Passa o outputFormat
                                                                                                 // como content_type
                                .buildAndExpand(automationRequestDto.id())
                                .encode() // Garante que os parâmetros sejam codificados corretamente
                                .toUriString();

                PythonTriggerResponseDTO pythonResponse = webClient.get()
                                .uri(pythonTriggerUri) // Usa a URI construída
                                .header("Authorization", "Bearer " + jwtToken)
                                .retrieve()
                                .onStatus(HttpStatusCode::is4xxClientError,
                                                clientResponse -> Mono.error(new ResponseStatusException(
                                                                clientResponse.statusCode(),
                                                                "Erro na API Python ao acionar trigger: "
                                                                                + automationRequestDto.id())))
                                .onStatus(HttpStatusCode::is5xxServerError,
                                                clientResponse -> Mono.error(new ResponseStatusException(
                                                                clientResponse.statusCode(),
                                                                "Erro interno da API Python ao acionar trigger: "
                                                                                + automationRequestDto.id())))
                                .bodyToMono(PythonTriggerResponseDTO.class)
                                .block();

                if (pythonResponse == null || pythonResponse.task_id() == null) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Resposta inválida da API Python para o trigger: " + automationRequestDto.id());
                }

                materialProcess.setTaskId(pythonResponse.task_id());
                materialProcess.setStatus(pythonResponse.status());
                materialProcess = materialProcessRepository.save(materialProcess);

                return materialProcessMapper.toResponseDto(materialProcess);
        }

        @Override
        public MaterialProcessResponseDTO getMaterialProcessDetails(String userId, String taskId) {
                MaterialProcess materialProcess = materialProcessRepository.findByUserIdAndTaskId(userId, taskId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "MaterialProcess não encontrado no Spring Boot para o taskId: "
                                                                + taskId));

                PythonMaterialResponseDTO pythonResponse = webClient.get()
                                .uri("/api/material-process/{task_id}", taskId)
                                .header("Authorization", "Bearer " + userId)
                                .retrieve()
                                .onStatus(HttpStatusCode::is4xxClientError,
                                                clientResponse -> Mono.error(new ResponseStatusException(
                                                                clientResponse.statusCode(),
                                                                "Erro na API Python ao buscar detalhes da tarefa: "
                                                                                + taskId)))
                                .onStatus(HttpStatusCode::is5xxServerError,
                                                clientResponse -> Mono.error(new ResponseStatusException(
                                                                clientResponse.statusCode(),
                                                                "Erro interno da API Python ao buscar detalhes da tarefa: "
                                                                                + taskId)))
                                .bodyToMono(PythonMaterialResponseDTO.class)
                                .block();

                if (pythonResponse == null) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Resposta vazia da API Python para o taskId: " + taskId);
                }

                materialProcessMapper.updateEntityFromPythonDto(pythonResponse, materialProcess);
                materialProcess = materialProcessRepository.save(materialProcess);

                return materialProcessMapper.toResponseDto(materialProcess);
        }

        @Override
        public MaterialProcessResponseDTO triggerManualContentGeneration(
                        String userId,
                        String taskId,
                        GenerateContentManualRequestDTO manualRequest) {
                MaterialProcess materialProcess = materialProcessRepository.findByUserIdAndTaskId(userId, taskId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "MaterialProcess não encontrado para o taskId: " + taskId));

                Map<String, Object> pythonPayload = objectMapper.convertValue(
                                manualRequest,
                                new TypeReference<Map<String, Object>>() {
                                });
                pythonPayload.put("user_id", userId);
                pythonPayload.put("task_id", taskId);

                Map<String, Object> pythonResponse = webClient.post()
                                .uri("/generate_content_manual_async")
                                .bodyValue(pythonPayload)
                                .retrieve()
                                .onStatus(HttpStatusCode::is4xxClientError,
                                                clientResponse -> Mono.error(new ResponseStatusException(
                                                                clientResponse.statusCode(),
                                                                "Erro na API Python ao acionar geração manual: "
                                                                                + taskId)))
                                .onStatus(HttpStatusCode::is5xxServerError,
                                                clientResponse -> Mono.error(new ResponseStatusException(
                                                                clientResponse.statusCode(),
                                                                "Erro interno da API Python ao acionar geração manual: "
                                                                                + taskId)))
                                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                                })
                                .block();

                if (pythonResponse == null || !"GENERATING".equals(pythonResponse.get("status"))) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Falha ao iniciar geração de conteúdo na API Python para o taskId: " + taskId);
                }

                materialProcess.setStatus("GENERATING");
                materialProcess = materialProcessRepository.save(materialProcess);

                return materialProcessMapper.toResponseDto(materialProcess);
        }

        @Override
        public void submitFinalPost(String userId, String taskId, SubmitFinalPostRequestDTO submitRequest) {
                Map<String, Object> pythonPayload = objectMapper.convertValue(
                                submitRequest,
                                new TypeReference<Map<String, Object>>() {
                                });

                webClient.post()
                                .uri("/submit_final_post")
                                .bodyValue(pythonPayload)
                                .retrieve()
                                .onStatus(HttpStatusCode::is4xxClientError,
                                                clientResponse -> Mono.error(new ResponseStatusException(
                                                                clientResponse.statusCode(),
                                                                "Erro na API Python ao submeter post final: "
                                                                                + taskId)))
                                .onStatus(HttpStatusCode::is5xxServerError,
                                                clientResponse -> Mono.error(new ResponseStatusException(
                                                                clientResponse.statusCode(),
                                                                "Erro interno da API Python ao submeter post final: "
                                                                                + taskId)))
                                .bodyToMono(Void.class)
                                .block();

                materialProcessRepository.findByUserIdAndTaskId(userId, taskId)
                                .ifPresent(materialProcessRepository::delete);
        }

        @Override
        public List<MaterialProcessResponseDTO> listMaterialProcesses(Optional<String> status) {
                List<MaterialProcess> materials;
                if (status.isPresent()) {
                        materials = materialProcessRepository.findByStatus(status.get());
                } else {
                        materials = materialProcessRepository.findAll();
                }

                return materials.stream()
                                .map(materialProcessMapper::toResponseDto)
                                .collect(Collectors.toList());
        }

        @Override
        public ImageGenerationResponseDTO generateImage(String imagePrompt, String jwtToken) {
                ImageGenerationRequestDTO requestBody = new ImageGenerationRequestDTO(imagePrompt);

                ImageGenerationResponseDTO pythonResponse = webClient.post()
                                .uri("/generate_image")
                                .header("Authorization", "Bearer " + jwtToken)
                                .bodyValue(requestBody)
                                .retrieve()
                                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono
                                                .error(new ResponseStatusException(clientResponse.statusCode(),
                                                                "Erro na API Python ao gerar imagem: " + imagePrompt)))
                                .onStatus(HttpStatusCode::is5xxServerError,
                                                clientResponse -> Mono.error(new ResponseStatusException(
                                                                clientResponse.statusCode(),
                                                                "Erro interno da API Python ao gerar imagem: "
                                                                                + imagePrompt)))
                                .bodyToMono(ImageGenerationResponseDTO.class)
                                .block();

                if (pythonResponse == null || pythonResponse.imageBase64() == null
                                || pythonResponse.imageBase64().isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Resposta inválida da API Python para geração de imagem.");
                }

                return pythonResponse;
        }
}
