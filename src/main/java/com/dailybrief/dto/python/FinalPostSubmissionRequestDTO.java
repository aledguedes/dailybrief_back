package com.dailybrief.dto.python;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public record FinalPostSubmissionRequestDTO(
        @NotBlank String userId,
        @NotBlank String taskId,
        @NotNull @JsonProperty("generated_content") Map<String, MultilingualContentDTO> generatedContent,
        String finalImagePrompt,
        String category,
        List<String> tags) {
}