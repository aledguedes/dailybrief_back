package com.dailybrief.service;

import com.dailybrief.dto.MaterialResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AutomationService {
    Page<MaterialResponseDTO> getAllMaterials(Pageable pageable);

    MaterialResponseDTO getMaterialById(String taskId);
}
