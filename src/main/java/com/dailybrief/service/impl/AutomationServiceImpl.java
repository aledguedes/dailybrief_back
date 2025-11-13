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
import com.dailybrief.mapper.RawMaterialMapper;
import com.dailybrief.model.Image;
import com.dailybrief.model.Material;
import com.dailybrief.model.Post;
import com.dailybrief.model.RawMaterial;
import com.dailybrief.model.Status;
import com.dailybrief.repository.MaterialRepository;
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
	private final PostRepository postRepository;
	private final RawMaterialRepository rawMaterialRepository;
	private final MaterialMapper materialMapper;
	private final RawMaterialMapper rawMaterialMapper;
	private final ObjectMapper objectMapper;

	public AutomationServiceImpl(Cloudinary cloudinary, StatusRepository statusRepository,
			MaterialRepository materialRepository, PostRepository postRepository,
			RawMaterialRepository rawMaterialRepository, MaterialMapper materialMapper,
			RawMaterialMapper rawMaterialMapper, ObjectMapper objectMapper) {
		this.cloudinary = cloudinary;
		this.postRepository = postRepository;
		this.statusRepository = statusRepository;
		this.materialRepository = materialRepository;
		this.rawMaterialRepository = rawMaterialRepository;
		this.materialMapper = materialMapper;
		this.rawMaterialMapper = rawMaterialMapper;
		this.objectMapper = objectMapper;
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

		return rawMaterials.stream().map(rawMaterialMapper::toResponse).collect(Collectors.toList());
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

		return rawMaterials.stream().map(rawMaterialMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public String exportRawMaterials(String taskId, String format) {

		Material material = materialRepository.findById(taskId)
				.orElseThrow(() -> new PostNotFoundException("Material not found with taskId: " + taskId));

		List<String> rawMaterialIds = material.getRawMaterialIds();

		List<RawMaterial> filteredRawMaterials = rawMaterialRepository.findAllById(rawMaterialIds);

		switch (format.toLowerCase()) {
		case "csv":
			return formatToCsv(filteredRawMaterials);

		case "json":
			return formatToJson(filteredRawMaterials);

		case "txt":
			return formatToTxt(filteredRawMaterials);

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

	private String formatToCsv(List<RawMaterial> rawMaterials) {
		String csvHeader = "ID,URL,Content_Preview,CreatedAt\n";
		String csvBody = rawMaterials.stream()
				.map(rm -> String.format("\"%s\",\"%s\",\"%s\",\"%s\"", rm.getId(), rm.getUrl(),
						rawMaterialMapper.truncateContent(rm.getContent()), rm.getCreatedAt()))
				.collect(Collectors.joining("\n"));
		return csvHeader + csvBody;
	}

	private String formatToJson(List<RawMaterial> rawMaterials) {
		List<?> responseDtos = rawMaterials.stream().map(rawMaterialMapper::toFullResponse)
				.collect(Collectors.toList());

		try {
			return objectMapper.writeValueAsString(responseDtos);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Erro ao serializar JSON para exportação", e);
		}
	}

	private String formatToTxt(List<RawMaterial> rawMaterials) {
		return rawMaterials.stream()
				.map(rm -> "ID: " + rm.getId() + "\nURL: " + rm.getUrl() + "\nContent:\n" + rm.getContent() + "\n---\n")
				.collect(Collectors.joining());
	}

}
