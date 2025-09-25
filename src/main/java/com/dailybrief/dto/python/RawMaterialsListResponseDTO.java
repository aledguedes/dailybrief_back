package com.dailybrief.dto.python;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record RawMaterialsListResponseDTO(
        @NotNull List<RawMaterialResponseDTO> rawMaterials) {
}