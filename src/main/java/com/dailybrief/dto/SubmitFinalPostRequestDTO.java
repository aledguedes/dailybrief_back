package com.dailybrief.dto;

import jakarta.validation.constraints.NotBlank;

public record SubmitFinalPostRequestDTO(
        @NotBlank String taskId) {
}