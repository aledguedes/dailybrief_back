package com.dailybrief.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.dailybrief.dto.AutomationDTO;
import com.dailybrief.mapper.AutomationMapper;
import com.dailybrief.model.Automation;
import com.dailybrief.repository.AutomationRepository;
import com.dailybrief.service.AutomationService;

@Service
public class AutomationServiceImpl implements AutomationService {

	private final AutomationRepository repository;
	private final AutomationMapper mapper;
	private final RestTemplate restTemplate;

	private static final String AUTOMATION_TRIGGER_URL = "http://localhost:8000/trigger-by-id/";

	@Autowired
	public AutomationServiceImpl(AutomationRepository repository, AutomationMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
		this.restTemplate = new RestTemplate();
	}

	@Override
	public String saveAutomationRequest(AutomationDTO dto, String jwtToken) {
		Automation entity = mapper.toEntity(dto);

		System.out.println("CHEGANDO NO SERVICE: " + entity.getOutputFormat());
		System.out.println("CHEGANDO NO SERVICE THEME: " + entity.getTheme());

		Long id = repository.save(entity).getId();

		String url = AUTOMATION_TRIGGER_URL + id;

		HttpHeaders headers = new HttpHeaders();

		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

		headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		try {

			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

			return response.getBody();
		} catch (HttpStatusCodeException e) {

			throw new RuntimeException("Erro ao chamar serviço externo (HTTP Status " + e.getStatusCode() + "): "
					+ e.getResponseBodyAsString(), e);
		} catch (ResourceAccessException e) {

			throw new RuntimeException(
					"Erro ao acessar o serviço externo (conexão recusada ou timeout): " + e.getMessage(), e);
		} catch (Exception e) {

			throw new RuntimeException("Erro inesperado ao chamar o serviço externo: " + e.getMessage(), e);
		}
	}

}
