package com.dailybrief.controller;

import com.dailybrief.dto.MaterialResponseDTO;
import com.dailybrief.dto.RawMaterialResponseDTO; // Novo Import
import com.dailybrief.service.AutomationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List; // Novo Import

@RestController
@RequestMapping("/api/automation")
@CrossOrigin(origins = "*")
public class AutomationController {

	private final AutomationService automationService;

	public AutomationController(AutomationService automationService) {
		this.automationService = automationService;
	}

	@Operation(summary = "Listar todos os materiais", description = "Lista todos os materiais com paginação (page e size)")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Materiais recuperados com sucesso"),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	@GetMapping
	public ResponseEntity<Page<MaterialResponseDTO>> getAllMaterials(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<MaterialResponseDTO> materials = automationService.getAllMaterials(PageRequest.of(page, size));
		return ResponseEntity.ok(materials);
	}

	@Operation(summary = "Obter material por taskId", description = "Busca um material pelo seu identificador único (taskId)")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Material recuperado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Material não encontrado") })
	@GetMapping("/materials/{taskId}")
	public ResponseEntity<MaterialResponseDTO> getMaterialById(@PathVariable String taskId) {
		MaterialResponseDTO material = automationService.getMaterialById(taskId);
		return ResponseEntity.ok(material);
	}

	@Operation(summary = "Obter conteúdo bruto das matérias-primas", description = "Busca o conteúdo (URL e texto) de todas as matérias-primas (RawMaterial) associadas a um Material específico.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Conteúdos brutos recuperados com sucesso"),
			@ApiResponse(responseCode = "404", description = "Material não encontrado para buscar as matérias-primas") })
	@GetMapping("/materials/{taskId}/raw-contents")
	public ResponseEntity<List<RawMaterialResponseDTO>> getRawMaterialsContent(@PathVariable String taskId) {

		List<RawMaterialResponseDTO> rawMaterials = automationService.getRawMaterialsContentByMaterialId(taskId);

		return ResponseEntity.ok(rawMaterials);
	}

	@Operation(summary = "Obter conteúdo bruto COMPLETO por ID", description = "Busca um RawMaterial específico pelo seu ID e retorna o campo 'content' na íntegra.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Conteúdo bruto recuperado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Matéria-prima não encontrada") })
	@GetMapping("/raw-materials/{rawMaterialId}")
	public ResponseEntity<RawMaterialResponseDTO> getRawMaterialContentById(@PathVariable String rawMaterialId) {

		RawMaterialResponseDTO rawMaterial = automationService.getRawMaterialContentById(rawMaterialId);

		return ResponseEntity.ok(rawMaterial);
	}
}