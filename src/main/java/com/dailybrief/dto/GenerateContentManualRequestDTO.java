package com.dailybrief.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record GenerateContentManualRequestDTO(
        @NotEmpty List<String> rawMaterialIds) {
}