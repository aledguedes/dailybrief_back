package com.dailybrief.dto.python;

import jakarta.validation.constraints.NotNull;

public record TriggerResponseDTO(
                String triggerId,
                @NotNull String message,
                @NotNull String taskId,
                @NotNull String status) {
}