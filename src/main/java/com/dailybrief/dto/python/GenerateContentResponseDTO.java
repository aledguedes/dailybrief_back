package com.dailybrief.dto.python;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record GenerateContentResponseDTO(
        @NotNull String taskId,
        @NotNull String status,
        String finalImagePrompt,
        String category,
        List<String> tags,
        @NotNull @JsonProperty("pt_br") MultilingualContentDTO ptBr,
        @NotNull @JsonProperty("en_us") MultilingualContentDTO enUs,
        @NotNull @JsonProperty("es_es") MultilingualContentDTO esEs) {
}