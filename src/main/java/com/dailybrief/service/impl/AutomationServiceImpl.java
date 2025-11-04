package com.dailybrief.service.impl;

import com.dailybrief.dto.MaterialResponseDTO;
import com.dailybrief.dto.RawMaterialResponseDTO;
import com.dailybrief.dto.RawMaterialUpdateDTO;
import com.dailybrief.exception.PostNotFoundException;
import com.dailybrief.exception.RawMaterialNotFoundException;
import com.dailybrief.mapper.MaterialMapper;
import com.dailybrief.mapper.RawMaterialMapper;
import com.dailybrief.model.Material;
import com.dailybrief.model.RawMaterial;
import com.dailybrief.repository.MaterialRepository;
import com.dailybrief.repository.RawMaterialRepository;
import com.dailybrief.service.AutomationService;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AutomationServiceImpl implements AutomationService {

	private final MaterialRepository materialRepository;
	private final RawMaterialRepository rawMaterialRepository;
	private final MaterialMapper materialMapper;
	private final RawMaterialMapper rawMaterialMapper;

	public AutomationServiceImpl(MaterialRepository materialRepository, RawMaterialRepository rawMaterialRepository,
			MaterialMapper materialMapper, RawMaterialMapper rawMaterialMapper) {
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

		Material material = materialRepository.findById(taskId)
				.orElseThrow(() -> new PostNotFoundException("Material not found with taskId: " + taskId));

		List<String> rawMaterialIds = material.getRawMaterialIds();

		if (rawMaterialIds == null || rawMaterialIds.isEmpty()) {
			return List.of();
		}

		List<RawMaterial> rawMaterials = rawMaterialRepository.findAllByIdIn(rawMaterialIds);

		return rawMaterials.stream().map(rawMaterialMapper::toResponse)
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

	/**
	 * Atualiza o campo 'content' de um RawMaterial específico.
	 *
	 * @param rawMaterialId ID da matéria-prima a ser atualizada.
	 * @param updateDTO     DTO contendo o novo conteúdo.
	 * @return O RawMaterialResponseDTO atualizado (com conteúdo completo).
	 */
	@Override
	@Transactional
	public RawMaterialResponseDTO updateRawMaterialContent(String rawMaterialId, RawMaterialUpdateDTO updateDTO) {

		RawMaterial rawMaterial = rawMaterialRepository.findById(rawMaterialId)
				.orElseThrow(() -> new RawMaterialNotFoundException(rawMaterialId));

		rawMaterial.setContent(updateDTO.content());

		RawMaterial updatedRawMaterial = rawMaterialRepository.save(rawMaterial);

		return rawMaterialMapper.toFullResponse(updatedRawMaterial);
	}
}
