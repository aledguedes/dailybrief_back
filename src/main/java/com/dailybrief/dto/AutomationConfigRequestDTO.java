package com.dailybrief.dto;

import java.time.ZonedDateTime;

public record AutomationConfigRequestDTO(
        String taskId,
        Integer statusId,
        String searchFactors,
        ZonedDateTime createdAt) {
}
