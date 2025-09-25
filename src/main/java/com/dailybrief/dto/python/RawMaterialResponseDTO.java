package com.dailybrief.dto.python;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record RawMaterialResponseDTO(
        @NotBlank @JsonProperty("raw_material_id") String rawMaterialId,
        @NotBlank String userId,
        @NotBlank String url,
        @NotBlank String content,
        @NotNull @JsonProperty("created_at") LocalDateTime createdAt) {
}