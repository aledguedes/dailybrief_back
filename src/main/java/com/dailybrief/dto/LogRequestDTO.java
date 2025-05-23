package com.dailybrief.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;

public record LogRequestDTO(
        @NotBlank(message = "Action cannot be blank") String action,
        Instant timestamp) {
}