package com.dailybrief.service;

import com.dailybrief.dto.MaterialResponseDTO;
import com.dailybrief.dto.MaterialStatusUpdateDTO;
import com.dailybrief.dto.RawMaterialResponseDTO;
import com.dailybrief.dto.RawMaterialUpdateDTO;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AutomationService {
	Page<MaterialResponseDTO> getAllMaterials(Pageable pageable);

	MaterialResponseDTO getMaterialById(String taskId);

	List<RawMaterialResponseDTO> getRawMaterialsContentByMaterialId(String taskId);

	RawMaterialResponseDTO getRawMaterialContentById(String rawMaterialId);

	RawMaterialResponseDTO updateRawMaterialContent(String rawMaterialId, RawMaterialUpdateDTO updateDTO);

	MaterialResponseDTO updateMaterialStatus(String taskId, MaterialStatusUpdateDTO updateDTO);

	List<RawMaterialResponseDTO> searchRawMaterials(String query);

	String exportRawMaterials(String taskId, String format);

	MaterialResponseDTO updateSuggestedImagePrompt(String taskId, String prompt);

	Map<?, ?> upload(MultipartFile file, Map<String, Object> options, String postId);

	Map<?, ?> destroy(String publicId, Map<String, Object> options);
}
