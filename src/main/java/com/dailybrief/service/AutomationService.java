package com.dailybrief.service;

import com.dailybrief.dto.python.*;
import reactor.core.publisher.Mono;

public interface AutomationService {

    Mono<TriggerResponseDTO> triggerByUrl(TriggerByUrlRequestDTO requestDTO, String auth_token);

    Mono<TriggerResponseDTO> triggerByText(TriggerByTextRequestDTO requestDTO, String auth_token);

    Mono<TriggerResponseDTO> triggerMultipleUrls(AutomationTriggerRequestDTO requestDTO, String auth_token);

    Mono<TriggerResponseDTO> triggerById(String id, String auth_token);

    Mono<GenerateContentResponseDTO> generateContent(ContentGenerationRequestDTO requestDTO, String auth_token);

    Mono<GenerateContentResponseDTO> generateContentManual(ContentGenerationRequestDTO requestDTO, String auth_token);

    Mono<GenerateContentResponseDTO> generateByTaskId(String taskId, String auth_token);

    Mono<GenerateContentResponseDTO> getTaskResult(String taskId, String auth_token);

    Mono<GenerateContentResponseDTO> generateImage(ContentGenerationRequestDTO requestDTO, String auth_token);

    Mono<RawMaterialsListResponseDTO> listUserMaterials(String auth_token);

    Mono<RawMaterialResponseDTO> getRawMaterial(String rawMaterialId, String auth_token);

    Mono<RawMaterialsListResponseDTO> getRawMaterialsByTask(String taskId, String auth_token);

}