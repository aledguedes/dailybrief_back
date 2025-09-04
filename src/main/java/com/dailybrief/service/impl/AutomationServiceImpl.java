package com.dailybrief.service.impl;

import com.dailybrief.dto.*;
import com.dailybrief.model.TriggerTask;
import com.dailybrief.mapper.TriggerTaskMapper;
import com.dailybrief.repository.TriggerTaskRepository;
import com.dailybrief.service.AutomationService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AutomationServiceImpl implements AutomationService {

    private final WebClient webClient;
    private final TriggerTaskRepository triggerTaskRepository;
    private final TriggerTaskMapper triggerTaskMapper;

    public AutomationServiceImpl(WebClient webClient, TriggerTaskRepository triggerTaskRepository,
            TriggerTaskMapper triggerTaskMapper) {
        this.webClient = webClient;
        this.triggerTaskRepository = triggerTaskRepository;
        this.triggerTaskMapper = triggerTaskMapper;
    }

    @Override
    public Mono<Void> triggerByUrl(String url) {
        return webClient.post()
                .uri("/trigger-by-url")
                .bodyValue(new TriggerByUrlRequestDTO(url))
                .retrieve()
                .bodyToMono(Void.class);
    }

    @Override
    public Mono<TaskResultDTO> getTaskResult(String taskId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/get_task_result")
                        .queryParam("task_id", taskId)
                        .build())
                .retrieve()
                .bodyToMono(TaskResultDTO.class);
    }

    @Override
    public Mono<Void> triggerMultipleUrls(List<String> urls) {
        return webClient.post()
                .uri("/trigger-multiple-urls")
                .bodyValue(new AutomationMultipleUrlsRequestDTO(urls))
                .retrieve()
                .bodyToMono(Void.class);
    }

    @Override
    public Mono<TriggerResponseDTO> triggerByText(String text) {
        return webClient.post()
                .uri("/trigger-by-text")
                .bodyValue(new TriggerByTextRequestDTO(text))
                .retrieve()
                .bodyToMono(TriggerResponseDTO.class)
                .flatMap(responseDTO -> {
                    TriggerTask task = new TriggerTask();
                    task.setTriggerId(UUID.fromString(responseDTO.triggerId()));
                    task.setTaskId(responseDTO.taskId());
                    task.setMessage(responseDTO.message());
                    task.setStatus(responseDTO.status());
                    return Mono.fromCallable(() -> triggerTaskRepository.save(task))
                            .subscribeOn(Schedulers.boundedElastic())
                            .thenReturn(responseDTO);
                });
    }

    @Override
    public Flux<TriggerTaskResponseDTO> getAllTriggerTasks() {
        return Mono.fromCallable(triggerTaskRepository::findAll)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapIterable(list -> list)
                .map(triggerTaskMapper::toDto);
    }

    @Override
    public Mono<Optional<TriggerTaskResponseDTO>> getTriggerTaskById(UUID id) {
        return Mono.fromCallable(() -> triggerTaskRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .map(optionalEntity -> optionalEntity.map(triggerTaskMapper::toDto));
    }

    @Override
    public Mono<RawMaterialsListResponseDTO> listUserMaterials(String userId) {
        return webClient.get()
                .uri("/list_user_materials")
                .header("user-id", userId)
                .retrieve()
                .bodyToMono(RawMaterialsListResponseDTO.class);
    }

    @Override
    public Mono<RawMaterialsListResponseDTO> listRawMaterialsByTask(String taskId) {
        return webClient.get()
                .uri("/raw-materials-by-task/{taskId}", taskId)
                .retrieve()
                .bodyToMono(RawMaterialsListResponseDTO.class);
    }

    @Override
    public Mono<TriggerResponseDTO> extractFromUrls(List<String> urls) {
        return webClient.post()
                .uri("/extract-from-urls")
                .bodyValue(new AutomationMultipleUrlsRequestDTO(urls))
                .retrieve()
                .bodyToMono(TriggerResponseDTO.class)
                .flatMap(responseDTO -> {
                    TriggerTask task = new TriggerTask();
                    task.setTriggerId(UUID.fromString(responseDTO.triggerId()));
                    task.setTaskId(responseDTO.taskId());
                    task.setMessage(responseDTO.message());
                    task.setStatus(responseDTO.status());
                    return Mono.fromCallable(() -> triggerTaskRepository.save(task))
                            .subscribeOn(Schedulers.boundedElastic())
                            .thenReturn(responseDTO);
                });
    }

    @Override
    public Mono<TriggerResponseDTO> generateContentManual(List<String> rawMaterialIds) {
        return webClient.post()
                .uri("/generate-content-manual")
                .bodyValue(new GenerateContentManualRequestDTO(rawMaterialIds))
                .retrieve()
                .bodyToMono(TriggerResponseDTO.class)
                .flatMap(responseDTO -> {
                    TriggerTask task = new TriggerTask();
                    task.setTriggerId(UUID.fromString(responseDTO.triggerId()));
                    task.setTaskId(responseDTO.taskId());
                    task.setMessage(responseDTO.message());
                    task.setStatus(responseDTO.status());
                    return Mono.fromCallable(() -> triggerTaskRepository.save(task))
                            .subscribeOn(Schedulers.boundedElastic())
                            .thenReturn(responseDTO);
                });
    }

    @Override
    public Mono<TriggerResponseDTO> generateContent(String taskId) {
        return webClient.post()
                .uri("/generate/{taskId}", taskId)
                .retrieve()
                .bodyToMono(TriggerResponseDTO.class)
                .flatMap(responseDTO -> {
                    TriggerTask task = new TriggerTask();
                    task.setTriggerId(UUID.fromString(responseDTO.triggerId()));
                    task.setTaskId(responseDTO.taskId());
                    task.setMessage(responseDTO.message());
                    task.setStatus(responseDTO.status());
                    return Mono.fromCallable(() -> triggerTaskRepository.save(task))
                            .subscribeOn(Schedulers.boundedElastic())
                            .thenReturn(responseDTO);
                });
    }

    @Override
    public Mono<RawContentResponseDTO> getRawMaterialContent(String rawMaterialId) {
        return webClient.get()
                .uri("/raw-material/{rawMaterialId}", rawMaterialId)
                .retrieve()
                .bodyToMono(RawContentResponseDTO.class);
    }

    @Override
    public Mono<Void> submitFinalPost(String taskId) {
        return webClient.post()
                .uri("/submit-final-post")
                .bodyValue(new SubmitFinalPostRequestDTO(taskId))
                .retrieve()
                .bodyToMono(Void.class);
    }

    @Override
    public Mono<Void> deleteMaterial(String taskId) {
        return webClient.delete()
                .uri("/delete-material/{taskId}", taskId)
                .retrieve()
                .bodyToMono(Void.class);
    }

    @Override
    public Mono<ImageUrlResponseDTO> generateImage(String taskId) {
        return webClient.post()
                .uri("/generate-image")
                .bodyValue(new SubmitFinalPostRequestDTO(taskId))
                .retrieve()
                .bodyToMono(ImageUrlResponseDTO.class);
    }
}