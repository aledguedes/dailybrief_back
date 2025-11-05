package com.dailybrief.controller;

import com.dailybrief.dto.MaterialResponseDTO;
import com.dailybrief.dto.MaterialStatusUpdateDTO;
import com.dailybrief.dto.RawMaterialResponseDTO;
import com.dailybrief.dto.RawMaterialUpdateDTO;
import com.dailybrief.service.AutomationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

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
	@GetMapping("/materials/list-all")
	public ResponseEntity<Page<MaterialResponseDTO>> getAllMaterials(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<MaterialResponseDTO> materials = automationService.getAllMaterials(PageRequest.of(page, size));
		return ResponseEntity.ok(materials);
	}

	@Operation(summary = "Obter material por taskId", description = "Busca um material pelo seu identificador único (taskId)")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Material recuperado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Material não encontrado") })
	@GetMapping("/materials/list-by-task-id/{taskId}")
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

	@Operation(summary = "Atualizar o conteúdo bruto por ID", description = "Atualiza o campo 'content' de um RawMaterial pelo seu ID.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Conteúdo bruto atualizado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Erro de validação ou ID inválido"),
			@ApiResponse(responseCode = "404", description = "Matéria-prima não encontrada") })
	@PutMapping("/raw-materials/{rawMaterialId}")
	public ResponseEntity<RawMaterialResponseDTO> updateRawMaterialContent(@PathVariable String rawMaterialId,
			@RequestBody @Valid RawMaterialUpdateDTO updateDTO) {

		RawMaterialResponseDTO updatedRawMaterial = automationService.updateRawMaterialContent(rawMaterialId,
				updateDTO);

		return ResponseEntity.ok(updatedRawMaterial);
	}

	@Operation(summary = "Atualiza o status de um Material", description = "Muda o status do material, recebendo o ID do novo status, e retorna o material atualizado.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
			@ApiResponse(responseCode = "400", description = "ID de Status inválido ou erro de validação"),
			@ApiResponse(responseCode = "404", description = "Material ou Status não encontrado")
	})
	@PutMapping("/materials/{taskId}/status")
	public ResponseEntity<MaterialResponseDTO> updateMaterialStatus(
			@PathVariable String taskId,
			@RequestBody @Valid MaterialStatusUpdateDTO updateDTO) {

		MaterialResponseDTO updatedMaterial = automationService.updateMaterialStatus(taskId, updateDTO);
		return ResponseEntity.ok(updatedMaterial);
	}

	@Operation(summary = "Busca por conteúdo ou URL em RawMaterials", description = "Retorna RawMaterials cujos campos content ou url contêm o termo de busca (parâmetro 'query').")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Resultados da busca recuperados com sucesso")
	})
	@GetMapping("/raw-materials/search")
	public ResponseEntity<List<RawMaterialResponseDTO>> searchRawMaterials(
			@RequestParam String query) {

		List<RawMaterialResponseDTO> results = automationService.searchRawMaterials(query);
		return ResponseEntity.ok(results);
	}

	@Operation(summary = "Exportar todos os RawMaterials", description = "Exporta todos os dados brutos nos formatos CSV, JSON ou TXT (parâmetro 'format'). Formato padrão: CSV.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Arquivo gerado e retornado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Formato de exportação inválido")
	})
	@GetMapping("/raw-materials/export")
	public ResponseEntity<String> exportRawMaterials(
			@RequestParam(defaultValue = "csv") String format) {

		String exportedData = automationService.exportRawMaterials(format);

		MediaType mediaType;
		String extension;

		switch (format.toLowerCase()) {
			case "json":
				mediaType = MediaType.APPLICATION_JSON;
				extension = "json";
				break;
			case "txt":
				mediaType = MediaType.TEXT_PLAIN;
				extension = "txt";
				break;
			case "csv":
			default:
				mediaType = MediaType.parseMediaType("text/csv");
				extension = "csv";
				break;
		}

		return ResponseEntity.ok()
				.contentType(mediaType)
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"raw_materials_export." + extension + "\"")
				.body(exportedData);
	}
}