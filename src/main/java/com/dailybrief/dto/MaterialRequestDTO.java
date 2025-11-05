package com.dailybrief.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record MaterialRequestDTO(
                String taskId,
                String userId,
                Integer statusId,
                String theme,
                String contentType,
                List<String> rawMaterialIds,
                String suggestedImagePrompt,
                ZonedDateTime createdAt,
                ZonedDateTime updatedAt,
                List<String> sourceUrls) {
}
