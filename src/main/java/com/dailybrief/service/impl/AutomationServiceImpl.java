package com.dailybrief.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dailybrief.dto.AutomationDTO;
import com.dailybrief.dto.AutomationResponseDTO;
import com.dailybrief.dto.TrendingTopicSuggestionDTO;
import com.dailybrief.exception.PostNotFoundException;
import com.dailybrief.mapper.AutomationMapper;
import com.dailybrief.mapper.TrendingTopicSuggestionMapper;
import com.dailybrief.model.Automation;
import com.dailybrief.model.PostStatus;
import com.dailybrief.model.TrendingTopicSuggestion;
import com.dailybrief.repository.AutomationRepository;
import com.dailybrief.repository.TrendingTopicSuggestionRepository;
import com.dailybrief.service.AutomationService;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class AutomationServiceImpl implements AutomationService {

	private final RestTemplate restTemplate;
	private final AutomationMapper automationMapper;
	private final TrendingTopicSuggestionMapper trendingMapper;
	private final AutomationRepository automationRepository;
	private final TrendingTopicSuggestionRepository trendingRepository;

	private static final String AUTOMATION_TRIGGER_URL = "http://localhost:8000/trigger-by-id/";

	public AutomationServiceImpl(
			AutomationRepository automationRepository,
			AutomationMapper automationMapper,
			TrendingTopicSuggestionRepository trendingRepository,
			TrendingTopicSuggestionMapper trendingMapper) {

		this.automationRepository = automationRepository;
		this.automationMapper = automationMapper;
		this.restTemplate = new RestTemplate();
		this.trendingRepository = trendingRepository;
		this.trendingMapper = trendingMapper;
	}

	@Override
	public String saveAutomationRequest(AutomationDTO dto, String jwtToken) {
		Automation entity = automationMapper.toEntity(dto);
		Long id = automationRepository.save(entity).getId();

		String url = AUTOMATION_TRIGGER_URL + id;

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		headers.setBearerAuth(jwtToken);

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
			return response.getBody();

		} catch (HttpStatusCodeException e) {
			throw new RuntimeException("Erro ao chamar serviço externo (HTTP " + e.getStatusCode() + "): "
					+ e.getResponseBodyAsString(), e);

		} catch (ResourceAccessException e) {
			throw new RuntimeException("Erro ao acessar o serviço externo (conexão recusada ou timeout): "
					+ e.getMessage(), e);

		} catch (Exception e) {
			throw new RuntimeException("Erro inesperado ao chamar o serviço externo: " + e.getMessage(), e);
		}
	}

	@Override
	public void saveSuggestions(List<TrendingTopicSuggestionDTO> dtos) {
		if (dtos == null || dtos.isEmpty()) {
			throw new IllegalArgumentException("A lista de sugestões não pode estar vazia.");
		}

		for (TrendingTopicSuggestionDTO dto : dtos) {
			Optional<TrendingTopicSuggestion> existing = trendingRepository.findByTopicName(dto.topicName());

			TrendingTopicSuggestion entity = existing.orElseGet(() -> trendingMapper.toEntity(dto));

			entity.setTopicName(dto.topicName());
			entity.setRelevanceReason(dto.relevanceReason());
			entity.setSource(dto.source());
			entity.setUrl(dto.url());
			entity.setStatus(PostStatus.valueOf(dto.status()));

			trendingRepository.save(entity);
		}
	}

	@Override
	public List<TrendingTopicSuggestionDTO> getSuggestionsByStatus(String status) {
		try {

			return trendingRepository.findByStatus(status)
					.stream()
					.map(trendingMapper::toDTO)
					.collect(Collectors.toList());

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Status inválido: " + status);
		}
	}

	@Override
	public TrendingTopicSuggestionDTO updateSuggestion(Long id, TrendingTopicSuggestionDTO dto) {
		TrendingTopicSuggestion entity = trendingRepository.findById(id)
				.orElseThrow(() -> new PostNotFoundException("Sugestão não encontrada: ID " + id));

		entity.setTopicName(dto.topicName());
		entity.setSource(dto.source());
		entity.setRelevanceReason(dto.relevanceReason());
		entity.setUrl(dto.url());
		entity.setStatus(PostStatus.valueOf(dto.status()));

		trendingRepository.save(entity);
		return trendingMapper.toDTO(entity);
	}

	@Override
	public Optional<AutomationResponseDTO> findById(Long id) {
		return automationRepository.findById(id)
				.map(automationMapper::toResponseDto);
	}
}
