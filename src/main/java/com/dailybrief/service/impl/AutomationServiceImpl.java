package com.dailybrief.service.impl;

import com.dailybrief.dto.MaterialResponseDTO;
import com.dailybrief.dto.RawMaterialResponseDTO;
import com.dailybrief.exception.PostNotFoundException;
import com.dailybrief.exception.RawMaterialNotFoundException;
import com.dailybrief.mapper.MaterialMapper;
import com.dailybrief.mapper.RawMaterialMapper;
import com.dailybrief.model.Material;
import com.dailybrief.model.RawMaterial;
import com.dailybrief.repository.MaterialRepository;
import com.dailybrief.repository.RawMaterialRepository;
import com.dailybrief.service.AutomationService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AutomationServiceImpl implements AutomationService {

	private final MaterialRepository materialRepository;
	private final RawMaterialRepository rawMaterialRepository; // Injeção do novo repositório
	private final MaterialMapper materialMapper;
	private final RawMaterialMapper rawMaterialMapper; // Injeção do novo mapper

	public AutomationServiceImpl(MaterialRepository materialRepository, RawMaterialRepository rawMaterialRepository, // Adicionar
																														// no
																														// construtor
			MaterialMapper materialMapper, RawMaterialMapper rawMaterialMapper // Adicionar no construtor
	) {
		this.materialRepository = materialRepository;
		this.rawMaterialRepository = rawMaterialRepository;
		this.materialMapper = materialMapper;
		this.rawMaterialMapper = rawMaterialMapper;
	}

	/**
	 * Lista todos os materiais com paginação
	 *
	 * @param pageable parâmetros de paginação (page, size, sort)
	 * @return página de MaterialResponseDTO
	 */
	@Override
	public Page<MaterialResponseDTO> getAllMaterials(Pageable pageable) {
		return materialRepository.findAll(pageable).map(materialMapper::toResponse);
	}

	/**
	 * Busca um material pelo taskId
	 *
	 * @param taskId identificador do material
	 * @return MaterialResponseDTO correspondente
	 * @throws PostNotFoundException se o material não for encontrado
	 */
	@Override
	public MaterialResponseDTO getMaterialById(String taskId) {
		Material material = materialRepository.findById(taskId)
				.orElseThrow(() -> new PostNotFoundException("Material not found with taskId: " + taskId));
		return materialMapper.toResponse(material);
	}

	/**
	 * Busca o conteúdo das matérias-primas de um material.
	 *
	 * @param taskId identificador do material
	 * @return Lista de RawMaterialResponseDTO
	 */
	@Override
	public List<RawMaterialResponseDTO> getRawMaterialsContentByMaterialId(String taskId) {
		// 1. Busca o Material para obter a lista de rawMaterialIds
		Material material = materialRepository.findById(taskId)
				.orElseThrow(() -> new PostNotFoundException("Material not found with taskId: " + taskId));

		List<String> rawMaterialIds = material.getRawMaterialIds();

		if (rawMaterialIds == null || rawMaterialIds.isEmpty()) {
			return List.of(); // Retorna lista vazia se não houver IDs
		}

		// 2. Busca todas as entidades RawMaterial pelos IDs
		List<RawMaterial> rawMaterials = rawMaterialRepository.findAllByIdIn(rawMaterialIds);

		// 3. Usa o MapStruct (rawMaterialMapper) para converter a lista de entidades em
		// DTOs
		return rawMaterials.stream().map(rawMaterialMapper::toResponse) // MapStruct em ação!
				.collect(Collectors.toList());
	}

	/**
	 * Busca um RawMaterial completo pelo seu ID.
	 *
	 * @param rawMaterialId identificador do RawMaterial
	 * @return RawMaterialResponseDTO com o conteúdo COMPLETO.
	 * @throws RawMaterialNotFoundException se o material bruto não for encontrado.
	 */
	@Override
	public RawMaterialResponseDTO getRawMaterialContentById(String rawMaterialId) {
		RawMaterial rawMaterial = rawMaterialRepository.findById(rawMaterialId).orElseThrow(
				() -> new RawMaterialNotFoundException("Raw Material not found with id: " + rawMaterialId));

		return rawMaterialMapper.toFullResponse(rawMaterial);
	}
}
