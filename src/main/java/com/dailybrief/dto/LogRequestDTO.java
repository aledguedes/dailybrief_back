package com.dailybrief.dto;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LogRequestDTO(
                @NotBlank(message = "Report ID cannot be blank") String reportId,
                @NotBlank(message = "Level cannot be blank") String level,
                @NotBlank(message = "Action cannot be blank") String action,

                Map<String, Object> details,

                @NotNull(message = "Timestamp cannot be null") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC") Instant timestamp) {
}