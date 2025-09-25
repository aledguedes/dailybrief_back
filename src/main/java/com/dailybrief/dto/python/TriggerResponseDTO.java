package com.dailybrief.dto.python;

import jakarta.validation.constraints.NotNull;

public record TriggerResponseDTO(
        @NotNull String triggerId,
        @NotNull String message,
        @NotNull String taskId,
        @NotNull String status) {
}