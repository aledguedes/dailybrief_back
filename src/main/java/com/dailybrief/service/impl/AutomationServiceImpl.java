package com.dailybrief.service.impl;

import com.dailybrief.dto.MaterialResponseDTO;
import com.dailybrief.exception.PostNotFoundException;
import com.dailybrief.mapper.MaterialMapper;
import com.dailybrief.model.Material;
import com.dailybrief.repository.MaterialRepository;
import com.dailybrief.service.AutomationService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AutomationServiceImpl implements AutomationService {

    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;

    public AutomationServiceImpl(MaterialRepository materialRepository, MaterialMapper materialMapper) {
        this.materialRepository = materialRepository;
        this.materialMapper = materialMapper;
    }

    /**
     * Lista todos os materiais com paginação
     *
     * @param pageable parâmetros de paginação (page, size, sort)
     * @return página de MaterialResponseDTO
     */
    @Override
    public Page<MaterialResponseDTO> getAllMaterials(Pageable pageable) {
        return materialRepository.findAll(pageable)
                .map(materialMapper::toResponse);
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
                .orElseThrow(() -> new PostNotFoundException(
                        "Material not found with taskId: " + taskId));
        return materialMapper.toResponse(material);
    }
}
