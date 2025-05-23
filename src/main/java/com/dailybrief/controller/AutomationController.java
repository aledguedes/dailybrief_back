package com.dailybrief.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailybrief.dto.AutomationDTO;
import com.dailybrief.service.AutomationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/automation")
public class AutomationController {

	private final AutomationService service;

	@Autowired
	public AutomationController(AutomationService service) {
		this.service = service;
	}

	@PostMapping("/trigger")
	public ResponseEntity<String> triggerAutomation(@RequestBody @Valid AutomationDTO dto, HttpServletRequest request) {
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		String jwtToken = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwtToken = authorizationHeader.substring(7);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("Token de autorização Bearer ausente ou inválido.");
		}

		try {
			String response = service.saveAutomationRequest(dto, jwtToken);
			return ResponseEntity.ok(response);
		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao acionar a automação: " + e.getMessage());
		}
	}

}