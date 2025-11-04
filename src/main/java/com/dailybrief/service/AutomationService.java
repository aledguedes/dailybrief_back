package com.dailybrief.service;

import com.dailybrief.dto.MaterialResponseDTO;
import com.dailybrief.dto.RawMaterialResponseDTO;
import com.dailybrief.dto.RawMaterialUpdateDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AutomationService {
	Page<MaterialResponseDTO> getAllMaterials(Pageable pageable);

	MaterialResponseDTO getMaterialById(String taskId);

	List<RawMaterialResponseDTO> getRawMaterialsContentByMaterialId(String taskId);

	RawMaterialResponseDTO getRawMaterialContentById(String rawMaterialId);
	
	RawMaterialResponseDTO updateRawMaterialContent(String rawMaterialId, RawMaterialUpdateDTO updateDTO);
}
