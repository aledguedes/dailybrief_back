package com.dailybrief.dto;

import java.time.ZonedDateTime;

public record AutomationConfigResponseDTO(
        String taskId,
        StatusDTO status,
        String searchFactors,
        ZonedDateTime createdAt) {
}
