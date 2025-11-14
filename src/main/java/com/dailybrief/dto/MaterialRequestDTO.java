package com.dailybrief.dto;

import java.time.ZonedDateTime;

public record MaterialRequestDTO(
        String taskId,
        String userId,
        Integer statusId,
        String theme,
        String contentType,
        String suggestedImagePrompt,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt) {
}