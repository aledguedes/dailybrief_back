package com.dailybrief.dto.python;

import jakarta.validation.constraints.NotNull;

public record ContentGenerationRequestDTO(
                @NotNull String taskId,
                @NotNull String userId) {
}