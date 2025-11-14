package com.dailybrief.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dailybrief.dto.MaterialResponseDTO;
import com.dailybrief.dto.MaterialStatusUpdateDTO;
import com.dailybrief.dto.RawMaterialResponseDTO;
import com.dailybrief.dto.RawMaterialUpdateDTO;
import com.dailybrief.exception.PostNotFoundException;
import com.dailybrief.exception.RawMaterialNotFoundException;
import com.dailybrief.mapper.MaterialMapper;
import com.dailybrief.mapper.MaterialSourceMapper;
import com.dailybrief.mapper.RawMaterialMapper;
import com.dailybrief.model.Image;
import com.dailybrief.model.Material;
import com.dailybrief.model.MaterialSource;
import com.dailybrief.model.Post;
import com.dailybrief.model.RawMaterial;
import com.dailybrief.model.Status;
import com.dailybrief.repository.MaterialRepository;
import com.dailybrief.repository.MaterialSourceRepository;
import com.dailybrief.repository.PostRepository;
import com.dailybrief.repository.RawMaterialRepository;
import com.dailybrief.repository.StatusRepository;
import com.dailybrief.service.AutomationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AutomationServiceImpl implements AutomationService {

	private final Cloudinary cloudinary;
	private final StatusRepository statusRepository;
	private final MaterialRepository materialRepository;
	private final RawMaterialRepository rawMaterialRepository;
	private final MaterialSourceRepository materialSourceRepository;
	private final PostRepository postRepository;
	private final MaterialMapper materialMapper;
	private final RawMaterialMapper rawMaterialMapper;
	private final MaterialSourceMapper materialSourceMapper;
	private final ObjectMapper objectMapper;

	public AutomationServiceImpl(Cloudinary cloudinary, StatusRepository statusRepository,
			MaterialRepository materialRepository, PostRepository postRepository,
			RawMaterialRepository rawMaterialRepository, MaterialSourceRepository materialSourceRepository,
			MaterialMapper materialMapper, RawMaterialMapper rawMaterialMapper, ObjectMapper objectMapper,
			MaterialSourceMapper materialSourceMapper) {
		this.cloudinary = cloudinary;
		this.postRepository = postRepository;
		this.statusRepository = statusRepository;
		this.materialRepository = materialRepository;
		this.rawMaterialRepository = rawMaterialRepository;
		this.materialSourceRepository = materialSourceRepository;
		this.materialMapper = materialMapper;
		this.rawMaterialMapper = rawMaterialMapper;
		this.objectMapper = objectMapper;
		this.materialSourceMapper = materialSourceMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MaterialResponseDTO> getAllMaterials(Pageable pageable) {
		return materialRepository.findAll(pageable).map(materialMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public MaterialResponseDTO getMaterialById(String taskId) {
		Material material = materialRepository.findById(taskId)
				.orElseThrow(() -> new PostNotFoundException("Material not found with taskId: " + taskId));
		return materialMapper.toResponse(material);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RawMaterialResponseDTO> getRawMaterialsContentByMaterialId(String taskId) {

		Material material = materialRepository.findById(taskId)
				.orElseThrow(() -> new PostNotFoundException("Material not found with taskId: " + taskId));

		List<MaterialSource> sourceLogs = material.getSourceLogs();

		if (sourceLogs.isEmpty()) {
			return List.of();
		}

		List<RawMaterial> rawMaterials = sourceLogs.stream()
				.filter(source -> "SUCCESS".equalsIgnoreCase(source.getStatus()))
				.filter(source -> source.getRawMaterial() != null).map(MaterialSource::getRawMaterial)
				.collect(Collectors.toList());

		if (rawMaterials.isEmpty()) {
			return List.of();
		}

		return rawMaterials.stream().map(rawMaterialMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
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

		if (updateDTO.content() != null && !updateDTO.content().trim().isEmpty()) {
			materialSourceRepository.findByRawMaterialId(rawMaterialId).ifPresent(materialSource -> {

				if ("FAILED".equalsIgnoreCase(materialSource.getStatus())) {
					materialSource.setStatus("SUCCESS");
					materialSourceRepository.save(materialSource);
				}
			});
		}

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

		return rawMaterials.stream().map(rawMaterialMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public String exportRawMaterials(String taskId, String format) {

		Material material = materialRepository.findById(taskId)
				.orElseThrow(() -> new PostNotFoundException("Material not found with taskId: " + taskId));

		List<MaterialSource> sourceLogs = material.getSourceLogs();

		if (sourceLogs == null || sourceLogs.isEmpty()) {
			return "";
		}

		List<MaterialSource> allSourceLogs = sourceLogs.stream().collect(Collectors.toList());

		if (allSourceLogs.isEmpty()) {
			return "";
		}

		switch (format.toLowerCase()) {
		case "csv":
			return formatToCsvForSourceLogs(allSourceLogs);
		case "json":
			return formatToJsonForSourceLogs(allSourceLogs);
		case "txt":
			return formatToTxtForSourceLogs(allSourceLogs);
		default:
			throw new IllegalArgumentException("Formato de exportação inválido: " + format);
		}
	}

	@Override
	public Map<?, ?> upload(MultipartFile file, Map<String, Object> options, String postId) {
		try {
			if (options == null) {
				options = ObjectUtils.emptyMap();
			}
			Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);

			String imageUrl = (String) uploadResult.get("secure_url");
			if (postId != null && imageUrl != null) {
				updatePostImage(postId, imageUrl);
			}

			return uploadResult;
		} catch (IOException e) {
			throw new RuntimeException("Falha ao enviar imagem para o Cloudinary", e);
		}
	}

	@Override
	public Map<?, ?> destroy(String publicId, Map<String, Object> options) {
		try {
			if (options == null) {
				options = ObjectUtils.emptyMap();
			}
			return cloudinary.uploader().destroy(publicId, options);
		} catch (IOException e) {
			throw new RuntimeException("Falha ao excluir imagem do Cloudinary", e);
		}
	}

	@Override
	public MaterialResponseDTO updateSuggestedImagePrompt(String taskId, String prompt) {
		Material material = materialRepository.findById(taskId)
				.orElseThrow(() -> new PostNotFoundException("Material not found with taskId: " + taskId));

		material.setSuggestedImagePrompt(prompt);
		materialRepository.save(material);

		return materialMapper.toResponse(material);
	}

	private void updatePostImage(String postId, String imageUrl) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " not found"));

		Image image = new Image();
		image.setUrl(imageUrl);
		image.setPost(post);

		post.getImages().add(image);
		postRepository.save(post);
	}

	private String formatToCsvForSourceLogs(List<MaterialSource> sourceLogs) {
		String csvHeader = "ID,URL,Status,RawMaterialId,CreatedAt\n";
		String csvBody = sourceLogs.stream()
				.map(source -> String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"", source.getId(), source.getUrl(),
						source.getStatus(), source.getRawMaterial() != null ? source.getRawMaterial().getId() : "N/A",
						source.getCreatedAt()))
				.collect(Collectors.joining("\n"));
		return csvHeader + csvBody;
	}

	private String formatToJsonForSourceLogs(List<MaterialSource> sourceLogs) {

		List<?> responseDtos = sourceLogs.stream().map(materialSourceMapper::toDto).collect(Collectors.toList());

		try {
			return objectMapper.writeValueAsString(responseDtos);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Erro ao serializar JSON para exportação", e);
		}
	}

	private String formatToTxtForSourceLogs(List<MaterialSource> sourceLogs) {
		return sourceLogs.stream()
				.map(source -> "URL: " + source.getUrl() + "\nStatus: " + source.getStatus() + "\nRawMaterial ID: "
						+ (source.getRawMaterial() != null ? source.getRawMaterial().getId() : "N/A") + "\n---\n")
				.collect(Collectors.joining());
	}
}