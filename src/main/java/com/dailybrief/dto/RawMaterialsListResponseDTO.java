package com.dailybrief.dto;

import java.util.List;

public record RawMaterialsListResponseDTO(
        List<RawMaterialResponseDTO> rawMaterials) {
}