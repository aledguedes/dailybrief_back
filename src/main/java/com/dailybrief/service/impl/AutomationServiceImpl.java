package com.dailybrief.service.impl;

import com.dailybrief.dto.MaterialResponseDTO;
import com.dailybrief.dto.MaterialStatusUpdateDTO;
import com.dailybrief.dto.RawMaterialResponseDTO;
import com.dailybrief.dto.RawMaterialUpdateDTO;
import com.dailybrief.exception.PostNotFoundException;
import com.dailybrief.exception.RawMaterialNotFoundException;
import com.dailybrief.mapper.MaterialMapper;
import com.dailybrief.mapper.RawMaterialMapper;
import com.dailybrief.model.Material;
import com.dailybrief.model.RawMaterial;
import com.dailybrief.model.Status;
import com.dailybrief.repository.MaterialRepository;
import com.dailybrief.repository.RawMaterialRepository;
import com.dailybrief.repository.StatusRepository;
import com.dailybrief.service.AutomationService;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AutomationServiceImpl implements AutomationService {

	private final StatusRepository statusRepository;
	private final MaterialRepository materialRepository;
	private final RawMaterialRepository rawMaterialRepository;
	private final MaterialMapper materialMapper;
	private final RawMaterialMapper rawMaterialMapper;

	public AutomationServiceImpl(StatusRepository statusRepository, MaterialRepository materialRepository,
			RawMaterialRepository rawMaterialRepository,
			MaterialMapper materialMapper, RawMaterialMapper rawMaterialMapper) {
		this.statusRepository = statusRepository;
		this.materialRepository = materialRepository;
		this.rawMaterialRepository = rawMaterialRepository;
		this.materialMapper = materialMapper;
		this.rawMaterialMapper = rawMaterialMapper;
	}

	@Override
	public Page<MaterialResponseDTO> getAllMaterials(Pageable pageable) {
		return materialRepository.findAll(pageable).map(materialMapper::toResponse);
	}

	@Override
	public MaterialResponseDTO getMaterialById(String taskId) {
		Material material = materialRepository.findById(taskId)
				.orElseThrow(() -> new PostNotFoundException("Material not found with taskId: " + taskId));
		return materialMapper.toResponse(material);
	}

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

	@Override
	public RawMaterialResponseDTO getRawMaterialContentById(String rawMaterialId) {
		RawMaterial rawMaterial = rawMaterialRepository.findById(rawMaterialId).orElseThrow(
				() -> new RawMaterialNotFoundException("Raw Material not found with id: " + rawMaterialId));

		return rawMaterialMapper.toFullResponse(rawMaterial);
	}

	@Override
	@Transactional
	public RawMaterialResponseDTO updateRawMaterialContent(String rawMaterialId, RawMaterialUpdateDTO updateDTO) {

		RawMaterial rawMaterial = rawMaterialRepository.findById(rawMaterialId)
				.orElseThrow(() -> new RawMaterialNotFoundException(rawMaterialId));

		rawMaterial.setContent(updateDTO.content());

		RawMaterial updatedRawMaterial = rawMaterialRepository.save(rawMaterial);

		return rawMaterialMapper.toFullResponse(updatedRawMaterial);
	}

	@Override
	@Transactional
	public MaterialResponseDTO updateMaterialStatus(String taskId, MaterialStatusUpdateDTO updateDTO) {
		Material material = materialRepository.findById(taskId)
				.orElseThrow(() -> new PostNotFoundException("Material not found with taskId: " + taskId));

		Status newStatus = statusRepository.findById(updateDTO.statusId())
				.orElseThrow(() -> new PostNotFoundException("Status not found with ID: " + updateDTO.statusId()));

		material.setStatus(newStatus);
		Material updatedMaterial = materialRepository.save(material);

		return materialMapper.toResponse(updatedMaterial);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RawMaterialResponseDTO> searchRawMaterials(String query) {
		if (query == null || query.isBlank()) {
			return List.of();
		}

		List<RawMaterial> rawMaterials = rawMaterialRepository
				.findByContentContainingIgnoreCaseOrUrlContainingIgnoreCase(query, query);

		return rawMaterials.stream()
				.map(rawMaterialMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public String exportRawMaterials(String format) {
		List<RawMaterial> allRawMaterials = rawMaterialRepository.findAll();

		switch (format.toLowerCase()) {
			case "csv":

				String csvHeader = "ID,URL,Content_Preview,CreatedAt\n";
				String csvBody = allRawMaterials.stream()
						.map(rm -> String.format("\"%s\",\"%s\",\"%s\",\"%s\"",
								rm.getId(),
								rm.getUrl(),

								rawMaterialMapper.truncateContent(rm.getContent()),
								rm.getCreatedAt()))
						.collect(Collectors.joining("\n"));
				return csvHeader + csvBody;

			case "json":

				return allRawMaterials.stream()
						.map(rawMaterialMapper::toFullResponse)
						.collect(Collectors.toList())
						.toString();

			case "txt":
				return allRawMaterials.stream()
						.map(rm -> "ID: " + rm.getId() + "\nURL: " + rm.getUrl() + "\nContent:\n" + rm.getContent()
								+ "\n---\n")
						.collect(Collectors.joining());

			default:
				throw new IllegalArgumentException("Formato de exportação inválido: " + format);
		}
	}
}
