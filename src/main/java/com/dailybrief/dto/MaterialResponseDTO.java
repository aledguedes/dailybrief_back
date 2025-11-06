package com.dailybrief.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record MaterialResponseDTO(
        String taskId,
        String userId,
        StatusDTO status,
        String theme,
        String contentType,
        List<String> rawMaterialIds,
        String suggestedImagePrompt,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt,
        List<String> sourceUrls) {
}
