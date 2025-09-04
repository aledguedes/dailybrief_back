package com.dailybrief.service;

import com.dailybrief.dto.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AutomationService {
    Mono<Void> triggerByUrl(String url);

    Mono<TaskResultDTO> getTaskResult(String taskId);

    Mono<Void> triggerMultipleUrls(List<String> urls);

    Mono<TriggerResponseDTO> triggerByText(String text);

    Flux<TriggerTaskResponseDTO> getAllTriggerTasks();

    Mono<Optional<TriggerTaskResponseDTO>> getTriggerTaskById(UUID id);

    Mono<RawMaterialsListResponseDTO> listUserMaterials(String userId);

    Mono<RawMaterialsListResponseDTO> listRawMaterialsByTask(String taskId);

    Mono<TriggerResponseDTO> extractFromUrls(List<String> urls);

    Mono<TriggerResponseDTO> generateContentManual(List<String> rawMaterialIds);

    Mono<TriggerResponseDTO> generateContent(String taskId);

    Mono<RawContentResponseDTO> getRawMaterialContent(String rawMaterialId);

    Mono<Void> submitFinalPost(String taskId);

    Mono<Void> deleteMaterial(String taskId);

    Mono<ImageUrlResponseDTO> generateImage(String taskId);
}