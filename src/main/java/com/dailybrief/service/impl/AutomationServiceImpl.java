package com.dailybrief.service.impl;

import com.dailybrief.dto.python.*;
import com.dailybrief.service.AutomationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AutomationServiceImpl implements AutomationService {

	private final WebClient pythonWebClient;

	public AutomationServiceImpl(@Qualifier("pythonWebClient") WebClient pythonWebClient) {
		this.pythonWebClient = pythonWebClient;
	}

	@Override
	public Mono<TriggerResponseDTO> triggerByUrl(TriggerByUrlRequestDTO requestDTO, String auth_token) {
		return pythonWebClient.post()
				.uri("/api/trigger-by-url")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + auth_token)
				.body(Mono.just(requestDTO), TriggerByUrlRequestDTO.class)
				.retrieve()
				.bodyToMono(TriggerResponseDTO.class);
	}

	@Override
	public Mono<TriggerResponseDTO> triggerByText(TriggerByTextRequestDTO requestDTO, String auth_token) {
		return pythonWebClient.post()
				.uri("/api/trigger-by-text")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + auth_token)
				.body(Mono.just(requestDTO), TriggerByTextRequestDTO.class)
				.retrieve()
				.bodyToMono(TriggerResponseDTO.class);
	}

	@Override
	public Mono<TriggerResponseDTO> triggerMultipleUrls(AutomationTriggerRequestDTO requestDTO, String auth_token) {
		return pythonWebClient.post()
				.uri("/api/trigger-multiple-urls")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + auth_token)
				.body(Mono.just(requestDTO), AutomationTriggerRequestDTO.class)
				.retrieve()
				.bodyToMono(TriggerResponseDTO.class);
	}

	@Override
	public Mono<TriggerResponseDTO> triggerById(String id, String auth_token) {
		return pythonWebClient.get()
				.uri("/api/trigger-by-id/{id}", id)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + auth_token)
				.retrieve()
				.bodyToMono(TriggerResponseDTO.class);
	}

	@Override
	public Mono<GenerateContentResponseDTO> generateContent(ContentGenerationRequestDTO requestDTO, String auth_token) {
		return pythonWebClient.post()
				.uri("/api/generate-content")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + auth_token)
				.body(Mono.just(requestDTO), ContentGenerationRequestDTO.class)
				.retrieve()
				.bodyToMono(GenerateContentResponseDTO.class);
	}

	@Override
	public Mono<GenerateContentResponseDTO> generateContentManual(ContentGenerationRequestDTO requestDTO,
			String auth_token) {
		return pythonWebClient.post()
				.uri("/api/generate-content-manual")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + auth_token)
				.body(Mono.just(requestDTO), ContentGenerationRequestDTO.class)
				.retrieve()
				.bodyToMono(GenerateContentResponseDTO.class);
	}

	@Override
	public Mono<GenerateContentResponseDTO> generateByTaskId(String taskId, String auth_token) {
		return pythonWebClient.post()
				.uri("/api/generate/{task_id}", taskId)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + auth_token)
				.retrieve()
				.bodyToMono(GenerateContentResponseDTO.class);
	}

	@Override
	public Mono<GenerateContentResponseDTO> getTaskResult(String taskId, String auth_token) {
		return pythonWebClient.get()
				.uri("/api/get-task-result?task_id={taskId}", taskId)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + auth_token)
				.retrieve()
				.bodyToMono(GenerateContentResponseDTO.class);
	}

	@Override
	public Mono<GenerateContentResponseDTO> generateImage(ContentGenerationRequestDTO requestDTO, String auth_token) {
		return pythonWebClient.post()
				.uri("/api/generate-image")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + auth_token)
				.body(Mono.just(requestDTO), ContentGenerationRequestDTO.class)
				.retrieve()
				.bodyToMono(GenerateContentResponseDTO.class);
	}

	@Override
	public Mono<RawMaterialsListResponseDTO> listUserMaterials(String auth_token) {
		return pythonWebClient.get()
				.uri("/api/list-user-materials")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + auth_token)
				.retrieve()
				.bodyToMono(RawMaterialsListResponseDTO.class);
	}

	@Override
	public Mono<RawMaterialResponseDTO> getRawMaterial(String rawMaterialId, String auth_token) {
		return pythonWebClient.get()
				.uri("/api/raw-material/{raw_material_id}", rawMaterialId)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + auth_token)
				.retrieve()
				.bodyToMono(RawMaterialResponseDTO.class);
	}

	@Override
	public Mono<RawMaterialsListResponseDTO> getRawMaterialsByTask(String taskId, String auth_token) {
		return pythonWebClient.get()
				.uri("/api/raw-materials-by-task/{task_id}", taskId)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + auth_token)
				.retrieve()
				.bodyToMono(RawMaterialsListResponseDTO.class);
	}
}