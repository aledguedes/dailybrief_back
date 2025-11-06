package com.dailybrief.dto;

import java.time.ZonedDateTime;

public record RawMaterialResponseDTO(
        String id,
        String userId,
        String taskId,
        String url,
        String content,
        ZonedDateTime createdAt) {
}
